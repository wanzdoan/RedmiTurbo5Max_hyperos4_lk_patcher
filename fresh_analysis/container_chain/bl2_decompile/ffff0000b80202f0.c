
undefined8 FUN_ffff0000b80202f0(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if (DAT_ffff0000b811b688 == 0) {
    uVar1 = 0xffffffda;
  }
  else {
    lVar2 = *(long *)(DAT_ffff0000b811b688 + 0x10);
    if (*(short *)(lVar2 + 0x12) == 0) {
      uVar1 = 0xffffffe8;
    }
    else {
      (**(code **)(DAT_ffff0000b811b688 + 8))
                (*(short *)(lVar2 + 0x12),1,*(undefined2 *)(lVar2 + 0x14),
                 *(undefined2 *)(lVar2 + 0x16));
      uVar1 = 0;
    }
  }
  return uVar1;
}


