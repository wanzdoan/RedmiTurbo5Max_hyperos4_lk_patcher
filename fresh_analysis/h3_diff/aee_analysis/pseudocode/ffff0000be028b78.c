
undefined8 FUN_ffff0000be028b78(void)

{
  long lVar1;
  undefined8 uVar2;
  
  if (DAT_ffff0000be117ea8 == 0) {
    uVar2 = 0xffffffda;
  }
  else {
    lVar1 = *(long *)(DAT_ffff0000be117ea8 + 0x10);
    (**(code **)(DAT_ffff0000be117ea8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),1,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    FUN_ffff0000be05ede4(0x3e);
    lVar1 = *(long *)(DAT_ffff0000be117ea8 + 0x10);
    (**(code **)(DAT_ffff0000be117ea8 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),0,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    uVar2 = 0;
  }
  return uVar2;
}


