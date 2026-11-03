// ffff000090798b90  size=176  FUN_ffff000090798b90

ulong FUN_ffff000090798b90(long *param_1,undefined4 param_2)

{
  uint uVar1;
  ulong uVar2;
  long lVar3;
  
  if (*param_1 == 0) {
    uVar2 = 0x10c004;
  }
  else {
    uVar2 = FUN_ffff0000907a4948();
    if ((int)uVar2 == 0) {
      FUN_ffff000090798ac4(param_1);
      FUN_ffff000090707ce4(*param_1,(int)param_1[2]);
      FUN_ffff000090707ce4((long)param_1 + 0x15,*(undefined1 *)((long)param_1 + 0x14));
      FUN_ffff000090707ce4((long)param_1 + 0x36,*(undefined1 *)((long)param_1 + 0x35));
      if (*(char *)((long)param_1 + 0x76) != '\0') {
        FUN_ffff000090707ce4((long)param_1 + 0x56);
        FUN_ffff000090707ce4((long)param_1 + 0x66,*(undefined1 *)((long)param_1 + 0x76));
      }
      lVar3 = FUN_ffff000090798c40(param_2);
      uVar1 = 0;
      if (lVar3 != 0) {
        uVar1 = 0x10c006;
      }
      uVar2 = (ulong)uVar1;
    }
  }
  return uVar2;
}


