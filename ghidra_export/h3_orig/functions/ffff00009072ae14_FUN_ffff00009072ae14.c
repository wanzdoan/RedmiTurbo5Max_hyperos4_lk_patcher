// ffff00009072ae14  size=80  FUN_ffff00009072ae14

void FUN_ffff00009072ae14(void)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff000090719218();
  if (lVar1 != 0) {
    FUN_ffff000090707ce4(lVar1,0x100);
    lVar2 = FUN_ffff00009071922c();
    if (lVar2 != 0) {
      FUN_ffff000090707ce4(lVar2,*(undefined4 *)(lVar1 + 0x1c));
      return;
    }
  }
  return;
}


