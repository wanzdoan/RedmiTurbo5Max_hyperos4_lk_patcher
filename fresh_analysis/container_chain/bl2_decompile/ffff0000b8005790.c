
void FUN_ffff0000b8005790(void)

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
  
  lVar3 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000);
  if (lVar3 == 0) {
    FUN_ffff0000b804c33c
              (s__s__d__Failed_to_get_regmap_ffff0000b808c29b,s_mt6379_auxadc_init_ffff0000b80989f8,
               0x8e);
    iVar2 = FUN_ffff0000b8027550();
  }
  else {
    iVar2 = FUN_ffff0000b8027550();
  }
  if (iVar2 != 0) {
    uVar8 = 0;
    puVar9 = &DAT_ffff0000b80ad64c;
    do {
      uVar4 = (ulong)DAT_ffff0000b80ad860;
      puVar6 = &DAT_ffff0000b80ad620 + uVar8 * 0x110;
      (&DAT_ffff0000b80ad718)[uVar8 * 0x22] = lVar3;
      FUN_ffff0000b804cd40(puVar6,&DAT_ffff0000b80a1f2c,uVar4 * 0x3c);
      uVar4 = (ulong)DAT_ffff0000b80ad860;
      puVar5 = puVar9;
      if (0 < (int)DAT_ffff0000b80ad860) {
        do {
          uVar4 = uVar4 - 1;
          puVar5[-4] = *(uint *)(&DAT_ffff0000b80ad720 + (ulong)puVar5[-4] * 4 + uVar8 * 0x110);
          uVar1 = *(uint *)(&DAT_ffff0000b80ad720 + (ulong)*puVar5 * 4 + uVar8 * 0x110);
          *puVar5 = uVar1;
          puVar5[-2] = uVar1 + 1;
          puVar5 = puVar5 + 0xf;
        } while (uVar4 != 0);
      }
      FUN_ffff0000b804cd40(&DAT_ffff0000b80ad6d8 + uVar8 * 0x22,&DAT_ffff0000b80ad850,0x40);
      (&DAT_ffff0000b80ad6f0)[uVar8 * 0x22] = puVar6;
      puVar7 = (&PTR_s_mt6379_auxadc_1_ffff0000b80ad840)[uVar8];
      (&DAT_ffff0000b80ad6d8)[uVar8 * 0x22] = puVar7;
      (&DAT_ffff0000b80ad6e0)[uVar8 * 0x22] = puVar6;
      iVar2 = FUN_ffff0000b8020b14(&DAT_ffff0000b80ad6d8 + uVar8 * 0x22);
      if (iVar2 != 0) {
        FUN_ffff0000b804c33c
                  (s__s__d__Failed_to_register__s_ffff0000b809d2a4,
                   s_mt6379_auxadc_init_ffff0000b80989f8,0x96,puVar7);
        return;
      }
      FUN_ffff0000b804c33c
                (s__s__d__Register__s_successfully_ffff0000b80950b6,
                 s_mt6379_auxadc_init_ffff0000b80989f8,0x99,puVar7);
      uVar8 = uVar8 + 1;
      uVar4 = FUN_ffff0000b8027550();
      puVar9 = puVar9 + 0x44;
    } while (uVar8 < (uVar4 & 0xffffffff));
  }
  FUN_ffff0000b804c33c
            (s__s__d__LK_init_done__ffff0000b80997d2,s_mt6379_auxadc_init_ffff0000b80989f8,0x9c);
  return;
}


