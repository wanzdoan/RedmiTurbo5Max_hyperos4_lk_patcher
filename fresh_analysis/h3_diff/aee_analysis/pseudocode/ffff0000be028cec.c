
undefined8 FUN_ffff0000be028cec(undefined1 param_1)

{
  long lVar1;
  
  if (DAT_ffff0000be117ea8 != 0) {
    lVar1 = *(long *)(DAT_ffff0000be117ea8 + 0x10);
    (**(code **)(DAT_ffff0000be117ea8 + 8))
              (*(undefined2 *)(lVar1 + 0x48),param_1,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
    return 0;
  }
  return 0xffffffda;
}


