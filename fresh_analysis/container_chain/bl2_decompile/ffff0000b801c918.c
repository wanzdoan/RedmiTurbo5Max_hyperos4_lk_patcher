
undefined8 FUN_ffff0000b801c918(void)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff0000b800c464();
  if (lVar1 != 0) {
    FUN_ffff0000b80075ac(lVar1,0x100);
    lVar2 = FUN_ffff0000b800c478();
    if (lVar2 != 0) {
      FUN_ffff0000b80075ac(lVar2,*(undefined4 *)(lVar1 + 0x1c));
      return 0;
    }
  }
  return 0xffffffff;
}


