
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b802c4f4(void)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined **ppuVar3;
  undefined **ppuVar4;
  undefined *puVar5;
  undefined **ppuVar6;
  
  FUN_ffff0000b804c33c(s_mtk_wdt_isr_ffff0000b808d279);
  FUN_ffff0000b802c344();
  _DAT_ffff00001c010020 = _DAT_ffff00001c01000c;
  if ((_DAT_ffff00001c010000 >> 5 & 1) == 0) {
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 | 0x22000020;
    FUN_ffff0000b804b704(0x46);
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffdf | 0x22000000;
  }
  else {
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffdf | 0x22000000;
    FUN_ffff0000b804b704(0x46);
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 | 0x22000020;
  }
  _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffff80f | 0x70;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  DAT_ffff0000b80ea4d8 = 1;
  ppuVar3 = (undefined **)PTR_LOOP_ffff0000b80ea4d0;
  while (ppuVar3 != &PTR_LOOP_ffff0000b80ea4c8) {
    ppuVar6 = (undefined **)ppuVar3[1];
    ppuVar4 = ppuVar6;
    if ((code *)ppuVar3[3] != (code *)0x0) {
      (*(code *)ppuVar3[3])();
      ppuVar4 = (undefined **)ppuVar3[1];
    }
    *ppuVar4 = *ppuVar3;
    puVar5 = *ppuVar3;
    *ppuVar3 = (undefined *)0x0;
    *(undefined ***)(puVar5 + 8) = ppuVar4;
    ppuVar3[1] = (undefined *)0x0;
    ppuVar3 = ppuVar6;
  }
  DAT_ffff0000b80ea4d8 = 0;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffa3 | 0x22000004;
  _DAT_ffff00001c010014 = 0x1209;
  FUN_ffff0000b804c33c(s__s__spinning_forever____ffff0000b8090db8,s_mtk_wdt_reset_ffff0000b8095630);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


