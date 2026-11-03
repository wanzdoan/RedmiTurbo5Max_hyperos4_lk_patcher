
undefined8 FUN_ffff0000b802027c(void)

{
  long lVar1;
  undefined8 uVar2;
  
  if (DAT_ffff0000b811b688 == 0) {
    uVar2 = 0xffffffda;
  }
  else {
    lVar1 = *(long *)(DAT_ffff0000b811b688 + 0x10);
    (**(code **)(DAT_ffff0000b811b688 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),1,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    FUN_ffff0000b804b704(0x3e);
    lVar1 = *(long *)(DAT_ffff0000b811b688 + 0x10);
    (**(code **)(DAT_ffff0000b811b688 + 8))
              (*(undefined2 *)(lVar1 + 0x2a),0,*(undefined2 *)(lVar1 + 0x2c),
               *(undefined2 *)(lVar1 + 0x2e));
    uVar2 = 0;
  }
  return uVar2;
}


