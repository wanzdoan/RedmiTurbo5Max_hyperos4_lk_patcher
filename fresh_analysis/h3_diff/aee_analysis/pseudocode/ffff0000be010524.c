
ulong FUN_ffff0000be010524(ulong param_1)

{
  uint uVar1;
  undefined8 uVar2;
  long lVar3;
  char *pcVar4;
  
  uVar2 = FUN_ffff0000be00ca18();
  if (0xbf000000 < param_1) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be102128,0,0x4000);
    lVar3 = FUN_ffff0000be00cbbc(uVar2,0x4000,&DAT_ffff0000be102128);
    if (lVar3 == 0x4000) {
      uVar1 = *(uint *)(&DAT_ffff0000be102128 + (param_1 >> 0x14 & 0xfff) * 4);
      if ((uVar1 & 3) == 2) {
        return (ulong)(uVar1 & 0xfff00000) | param_1 & 0xfffff;
      }
      if ((uVar1 & 3) != 1) {
        return 0;
      }
      FUN_ffff0000be060ae0(&DAT_ffff0000be106128,0,0x800);
      lVar3 = FUN_ffff0000be00cbbc(uVar1 & 0xfffff000,0x800,&DAT_ffff0000be106128);
      if (lVar3 == 0x800) {
        if ((*(uint *)(&DAT_ffff0000be106128 + (param_1 >> 0xc & 0x1ff) * 4) & 1) != 0) {
          return (ulong)*(uint *)(&DAT_ffff0000be106128 + (param_1 >> 0xc & 0x1ff) * 4) & 0xfffff000
                 | param_1 & 0xfff;
        }
        return 0;
      }
      pcVar4 = s_invalid_l2_page_table_ffff0000be0b857e;
    }
    else {
      pcVar4 = s_invalid_l1_page_table_ffff0000be0a3140;
    }
    FUN_ffff0000be063b40(pcVar4);
    FUN_ffff0000be05fd98(pcVar4);
  }
  return 0;
}


