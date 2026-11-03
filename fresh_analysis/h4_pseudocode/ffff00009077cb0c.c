
void FUN_ffff00009077cb0c(void)

{
  undefined **ppuVar1;
  undefined8 *puVar2;
  undefined *puVar3;
  undefined **ppuVar4;
  long lVar5;
  
  FUN_ffff00009077e11c(s_block_devices__ffff0000907e1f1c);
  FUN_ffff000090717738(&DAT_ffff0000908328f0,0xffffffff);
  ppuVar4 = (undefined **)PTR_LOOP_ffff0000908328e8;
  if ((undefined **)PTR_LOOP_ffff0000908328e8 != &PTR_LOOP_ffff0000908328e0) {
    do {
      FUN_ffff00009077e11c
                (s__s__size__lld__bsize__zd__ref__d_ffff0000907fa824,ppuVar4[3],ppuVar4[4],
                 ppuVar4[5],*(undefined4 *)(ppuVar4 + 2),ppuVar4[8]);
      if ((ppuVar4[0xb] == (undefined *)0x0) || (ppuVar4[0xc] == (undefined *)0x0)) {
        FUN_ffff00009077e11c(s__no_erase_geometry__ffff0000907eece5);
      }
      else {
        lVar5 = 0;
        puVar3 = (undefined *)0x0;
        do {
          puVar2 = (undefined8 *)(ppuVar4[0xc] + lVar5);
          FUN_ffff00009077e11c
                    (s_erase_region__zu____start__lld_s_ffff0000907f7248,puVar3,*puVar2,puVar2[1],
                     puVar2[2]);
          puVar3 = puVar3 + 1;
          lVar5 = lVar5 + 0x20;
        } while (puVar3 < ppuVar4[0xb]);
      }
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
      ppuVar1 = ppuVar4 + 1;
      ppuVar4 = (undefined **)*ppuVar1;
    } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff0000908328e0);
  }
  FUN_ffff0000907177d0(&DAT_ffff0000908328f0);
  return;
}


