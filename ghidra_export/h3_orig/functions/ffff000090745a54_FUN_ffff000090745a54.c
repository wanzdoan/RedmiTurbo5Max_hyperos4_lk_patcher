// ffff000090745a54  size=108  FUN_ffff000090745a54

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745a54(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  
  if (_DAT_ffff0000908774f8 != (undefined8 *)0x0) {
    FUN_ffff00009077eb88(*_DAT_ffff0000908774f8,0,*(undefined4 *)(_DAT_ffff0000908774f8 + 1));
    if (_DAT_ffff0000908774f8 == (undefined8 *)0x0) {
      uVar1 = 0;
      uVar2 = 0;
    }
    else {
      uVar1 = *(undefined4 *)((long)_DAT_ffff0000908774f8 + 0x14);
      uVar2 = *(undefined4 *)(_DAT_ffff0000908774f8 + 3);
    }
    FUN_ffff00009075c518(0,0,uVar1,uVar2);
    return;
  }
  return;
}


