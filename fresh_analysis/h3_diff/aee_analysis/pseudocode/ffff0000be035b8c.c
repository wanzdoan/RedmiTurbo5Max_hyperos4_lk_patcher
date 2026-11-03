
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be035b8c(void)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined **ppuVar3;
  undefined **ppuVar4;
  undefined *puVar5;
  undefined **ppuVar6;
  
  FUN_ffff0000be05fd98(s_mtk_wdt_isr_ffff0000be0ad947);
  FUN_ffff0000be0359dc();
  _DAT_ffff00001c010020 = _DAT_ffff00001c01000c;
  if ((_DAT_ffff00001c010000 >> 5 & 1) == 0) {
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 | 0x22000020;
    FUN_ffff0000be05ede4(0x46);
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffdf | 0x22000000;
  }
  else {
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffdf | 0x22000000;
    FUN_ffff0000be05ede4(0x46);
    _DAT_ffff00001c010000 = _DAT_ffff00001c010000 | 0x22000020;
  }
  _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffff80f | 0x80;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  DAT_ffff0000be0d75a8 = 1;
  ppuVar3 = (undefined **)PTR_LOOP_ffff0000be0d75a0;
  while (ppuVar3 != &PTR_LOOP_ffff0000be0d7598) {
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
  DAT_ffff0000be0d75a8 = 0;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  _DAT_ffff00001c010000 = _DAT_ffff00001c010000 & 0xddffffa3 | 0x22000004;
  _DAT_ffff00001c010014 = 0x1209;
  FUN_ffff0000be05fd98(s__s__spinning_forever____ffff0000be0b2074,s_mtk_wdt_reset_ffff0000be0b7901);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


