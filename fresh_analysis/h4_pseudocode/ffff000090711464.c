
void FUN_ffff000090711464(void)

{
  uint uVar1;
  uint uVar2;
  byte bVar3;
  int iVar4;
  
  bVar3 = FUN_ffff000090710bc0();
  uVar1 = (uint)bVar3;
  uVar2 = uVar1;
  if (bVar3 != 0) {
    if (uVar1 == 8) {
      uVar2 = 2;
    }
    else {
      uVar2 = DAT_ffff00009083f664;
      if (uVar1 == 2) {
        uVar2 = 1;
      }
    }
  }
  DAT_ffff00009083f664 = uVar2;
  FUN_ffff00009077ee50(&DAT_ffff00009083f668,0,0x100);
  iVar4 = FUN_ffff00009077f6fc
                    (&DAT_ffff00009083f668,0x100,s_block2mtd_block2mtd__dev_block_b_ffff0000907f4050
                     ,0x200000,DAT_ffff00009083f664,0,0x200000,1,1,DAT_ffff00009083f664);
  if (iVar4 < 0x100) {
    FUN_ffff00009077e11c(s_mtdoops_cmdline_is__s_ffff0000907f93c1,&DAT_ffff00009083f668);
    iVar4 = FUN_ffff00009077f250(&DAT_ffff00009083f668);
    if (iVar4 != 0) {
      FUN_ffff000090719078(&DAT_ffff00009083f668);
      return;
    }
  }
  else {
    FUN_ffff00009077e11c(s_Buffer_is_too_small_to_store_the_ffff0000908004b0);
  }
  FUN_ffff00009077e11c(s_get_mtdoops_cmdline_fail__ffff0000907da606);
  return;
}


