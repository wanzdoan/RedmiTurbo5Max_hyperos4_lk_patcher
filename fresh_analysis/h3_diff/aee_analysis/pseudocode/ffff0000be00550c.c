
void FUN_ffff0000be00550c(void)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  uint *puVar5;
  undefined *puVar6;
  undefined *puVar7;
  ulong uVar8;
  uint *puVar9;
  
  lVar3 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e);
  if (lVar3 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__Failed_to_get_regmap_ffff0000be0ac331,s_mt6379_auxadc_init_ffff0000be0bba4f,
               0x8e);
    iVar2 = FUN_ffff0000be031548();
  }
  else {
    iVar2 = FUN_ffff0000be031548();
  }
  if (iVar2 != 0) {
    uVar8 = 0;
    puVar9 = &DAT_ffff0000be0d569c;
    do {
      uVar4 = (ulong)DAT_ffff0000be0d58b0;
      puVar6 = &DAT_ffff0000be0d5670 + uVar8 * 0x110;
      (&DAT_ffff0000be0d5768)[uVar8 * 0x22] = lVar3;
      FUN_ffff0000be0607b0(puVar6,&DAT_ffff0000be0c74a4,uVar4 * 0x3c);
      uVar4 = (ulong)DAT_ffff0000be0d58b0;
      puVar5 = puVar9;
      if (0 < (int)DAT_ffff0000be0d58b0) {
        do {
          uVar4 = uVar4 - 1;
          puVar5[-4] = *(uint *)(&DAT_ffff0000be0d5770 + (ulong)puVar5[-4] * 4 + uVar8 * 0x110);
          uVar1 = *(uint *)(&DAT_ffff0000be0d5770 + (ulong)*puVar5 * 4 + uVar8 * 0x110);
          *puVar5 = uVar1;
          puVar5[-2] = uVar1 + 1;
          puVar5 = puVar5 + 0xf;
        } while (uVar4 != 0);
      }
      FUN_ffff0000be0607b0(&DAT_ffff0000be0d5728 + uVar8 * 0x22,&DAT_ffff0000be0d58a0,0x40);
      (&DAT_ffff0000be0d5740)[uVar8 * 0x22] = puVar6;
      puVar7 = (&PTR_s_mt6379_auxadc_1_ffff0000be0d5890)[uVar8];
      (&DAT_ffff0000be0d5728)[uVar8 * 0x22] = puVar7;
      (&DAT_ffff0000be0d5730)[uVar8 * 0x22] = puVar6;
      iVar2 = FUN_ffff0000be029260(&DAT_ffff0000be0d5728 + uVar8 * 0x22);
      if (iVar2 != 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__Failed_to_register__s_ffff0000be0c163d,
                   s_mt6379_auxadc_init_ffff0000be0bba4f,0x96,puVar7);
        return;
      }
      FUN_ffff0000be05fd98
                (s__s__d__Register__s_successfully_ffff0000be0b7239,
                 s_mt6379_auxadc_init_ffff0000be0bba4f,0x99,puVar7);
      uVar8 = uVar8 + 1;
      uVar4 = FUN_ffff0000be031548();
      puVar9 = puVar9 + 0x44;
    } while (uVar8 < (uVar4 & 0xffffffff));
  }
  FUN_ffff0000be05fd98
            (s__s__d__LK_init_done__ffff0000be0bcb75,s_mt6379_auxadc_init_ffff0000be0bba4f,0x9c);
  return;
}


