// ffff000090745cec  size=108  FUN_ffff000090745cec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745cec(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  
  if (_DAT_ffff000090877590 != (undefined8 *)0x0) {
    FUN_ffff00009077ee50(*_DAT_ffff000090877590,0,*(undefined4 *)(_DAT_ffff000090877590 + 1));
    if (_DAT_ffff000090877590 == (undefined8 *)0x0) {
      uVar1 = 0;
      uVar2 = 0;
    }
    else {
      uVar1 = *(undefined4 *)((long)_DAT_ffff000090877590 + 0x14);
      uVar2 = *(undefined4 *)(_DAT_ffff000090877590 + 3);
    }
    FUN_ffff00009075c7b0(0,0,uVar1,uVar2);
    return;
  }
  return;
}


