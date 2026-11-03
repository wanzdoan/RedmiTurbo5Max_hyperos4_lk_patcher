
undefined8 FUN_ffff0000be024a5c(void)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff0000be01be34();
  if (lVar1 != 0) {
    FUN_ffff0000be007328(lVar1,0x100);
    lVar2 = FUN_ffff0000be01be48();
    if (lVar2 != 0) {
      FUN_ffff0000be007328(lVar2,*(undefined4 *)(lVar1 + 0x1c));
      return 0;
    }
  }
  return 0xffffffff;
}


