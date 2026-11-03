
void FUN_ffff0000be05ea50(void)

{
  undefined **ppuVar1;
  undefined8 *puVar2;
  undefined *puVar3;
  undefined **ppuVar4;
  long lVar5;
  
  FUN_ffff0000be05fd98(s_block_devices__ffff0000be0b0424);
  FUN_ffff0000be01a730(&DAT_ffff0000be0ef2e8,0xffffffff);
  ppuVar4 = (undefined **)PTR_LOOP_ffff0000be0ef2e0;
  if ((undefined **)PTR_LOOP_ffff0000be0ef2e0 != &PTR_LOOP_ffff0000be0ef2d8) {
    do {
      FUN_ffff0000be05fd98
                (s__s__size__lld__bsize__zd__ref__d_ffff0000be0c11a2,ppuVar4[3],ppuVar4[4],
                 ppuVar4[5],*(undefined4 *)(ppuVar4 + 2),ppuVar4[8]);
      if ((ppuVar4[0xb] == (undefined *)0x0) || (ppuVar4[0xc] == (undefined *)0x0)) {
        FUN_ffff0000be05fd98(s__no_erase_geometry__ffff0000be0b91c3);
      }
      else {
        lVar5 = 0;
        puVar3 = (undefined *)0x0;
        do {
          puVar2 = (undefined8 *)(ppuVar4[0xc] + lVar5);
          FUN_ffff0000be05fd98
                    (s_erase_region__zu____start__lld_s_ffff0000be0bec90,puVar3,*puVar2,puVar2[1],
                     puVar2[2]);
          puVar3 = puVar3 + 1;
          lVar5 = lVar5 + 0x20;
        } while (puVar3 < ppuVar4[0xb]);
      }
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
      ppuVar1 = ppuVar4 + 1;
      ppuVar4 = (undefined **)*ppuVar1;
    } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff0000be0ef2d8);
  }
  FUN_ffff0000be01a7c8(&DAT_ffff0000be0ef2e8);
  return;
}


