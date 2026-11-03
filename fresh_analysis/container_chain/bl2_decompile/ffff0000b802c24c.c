
/* WARNING: Removing unreachable block (ram,0xffff0000b802c2dc) */
/* WARNING: Removing unreachable block (ram,0xffff0000b802c2f8) */
/* WARNING: Removing unreachable block (ram,0xffff0000b802c2e8) */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b802c24c(int param_1)

{
  uint uVar1;
  
  uVar1 = _DAT_ffff00001c010000;
  if (param_1 == 9) {
    if ((_DAT_ffff00001c010024 & 0x7f0) == 0) {
      _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffff80f | 0x70;
    }
  }
  else if (param_1 == 7) {
    _DAT_ffff00001c010048 = 0x95000000;
    FUN_ffff0000b804c33c(s_RGU_DFD_V2_ffff0000b8096558);
    _DAT_ffff00001c010048 = 0x90000000;
  }
  _DAT_ffff00001c010000 = uVar1 & 0xddffffa3 | 0x22000004;
  _DAT_ffff00001c010014 = 0x1209;
  FUN_ffff0000b804c33c(s__s__spinning_forever____ffff0000b8090db8,s_mtk_wdt_reset_ffff0000b8095630);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


