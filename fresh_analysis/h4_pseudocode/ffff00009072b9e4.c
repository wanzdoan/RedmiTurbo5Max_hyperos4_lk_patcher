
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072b9e4(void)

{
  undefined8 uVar1;
  uint uVar2;
  
  uVar2 = _DAT_ffff00000c000b48;
  if (_DAT_ffff00000c000b48 == 0xdeadbeef) {
    uVar2 = _DAT_ffff00000c1b0b48;
  }
  uVar1 = 0xfffffffe;
  switch(uVar2 >> 3 & 0x3f) {
  case 0:
  case 0x13:
  case 0x1d:
    _DAT_ffff00001c004000 = 0xb160001;
    _DAT_ffff00001c004288 = _DAT_ffff00001c004288 & 0xfffffffb | 3;
    if (((((((int)_DAT_ffff00001c004288 < 0) ||
           (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)) ||
          (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)) ||
         (((uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0 ||
           (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)) ||
          ((uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0 ||
           ((uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0 ||
            (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)))))))) ||
        ((uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0 ||
         (((uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0 ||
           (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)) ||
          (uVar1 = FUN_ffff00009077cea0(1), (int)_DAT_ffff00001c004288 < 0)))))) &&
       (((((_DAT_ffff00001c004288 = _DAT_ffff00001c004288 & 0xfffffffe,
           -1 < (int)_DAT_ffff00001c004288 ||
           (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)) ||
          ((uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288 ||
           ((uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288 ||
            (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)))))) ||
         (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)) ||
        (((((uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288 ||
            (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)) ||
           (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)) ||
          ((uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288 ||
           (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)))) ||
         (uVar1 = FUN_ffff00009077cea0(1), -1 < (int)_DAT_ffff00001c004288)))))) {
      FUN_ffff00009072b8b8(uVar1);
      uVar1 = 0;
    }
    else {
      uVar1 = 0xffffffff;
    }
    return uVar1;
  case 0xf:
    FUN_ffff00009072b8b8(0xfffffffe);
    uVar1 = 0;
  }
  return uVar1;
}


