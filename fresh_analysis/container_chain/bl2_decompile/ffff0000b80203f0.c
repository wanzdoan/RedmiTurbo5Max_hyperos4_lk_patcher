
undefined8 FUN_ffff0000b80203f0(undefined1 param_1)

{
  long lVar1;
  
  if (DAT_ffff0000b811b688 != 0) {
    lVar1 = *(long *)(DAT_ffff0000b811b688 + 0x10);
    (**(code **)(DAT_ffff0000b811b688 + 8))
              (*(undefined2 *)(lVar1 + 0x48),param_1,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
    return 0;
  }
  return 0xffffffda;
}


