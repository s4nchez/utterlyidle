(function(){
    var findChildrenByClassName = function(doc, className){
        var result = [];
        for (var i = 0; i < doc.childNodes.length; i++) {
            if (doc.childNodes[i].className == className) {
                result.push(doc.childNodes[i]);
            }
        }
        return result;
    };

    var watchForValueChanges = function(elements, callback){
        for (var i = 0; i < elements.length; i++) {
            elements[i].addEventListener('keyup', callback);
        }
    };

    var resolvePathUsingParameters = function(pathElements) {
        var result = '';
        for (var i = 0; i < pathElements.length; i++) {
            result = result + '/' + pathElements[i].value;
        }
        return result;
    };

    var createValueChangeHandler = function(form, pathSegments){
        return function() {
            form.action = resolvePathUsingParameters(pathSegments);
        };
    };

    var populatePathParameters = function(containerElements){
        for (var i = 0; i < containerElements.length; i++) {
            var form = containerElements[i];
            var pathSegments = findChildrenByClassName(form, 'pathSegment');
            watchForValueChanges(pathSegments, createValueChangeHandler(form, pathSegments));
        }
    };

    document.onreadystatechange = function () {
        if (document.readyState == "complete") {
            populatePathParameters(document.getElementsByClassName('binding'));
        }
    };
})();
