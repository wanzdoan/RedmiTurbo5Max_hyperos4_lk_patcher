
undefined8 FUN_ffff00009072b0a8(void)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff00009071935c();
  if (lVar1 != 0) {
    FUN_ffff000090707c70(lVar1,0x100);
    lVar2 = FUN_ffff000090719370();
    if (lVar2 != 0) {
      FUN_ffff000090707c70(lVar2,*(undefined4 *)(lVar1 + 0x1c));
      return 0;
    }
  }
  return 0xffffffff;
}


