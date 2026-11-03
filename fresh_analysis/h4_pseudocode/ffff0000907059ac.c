
void FUN_ffff0000907059ac(void)

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
  
  lVar3 = FUN_ffff000090737168(s_mt6379_ffff0000907ea569);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_get_regmap_ffff0000907dbe95,s_mt6379_auxadc_init_ffff0000907f2579,
               0x8e);
    iVar2 = FUN_ffff0000907414e4();
  }
  else {
    iVar2 = FUN_ffff0000907414e4();
  }
  if (iVar2 != 0) {
    uVar8 = 0;
    puVar9 = &DAT_ffff00009081664c;
    do {
      uVar4 = (ulong)DAT_ffff000090816860;
      puVar6 = &DAT_ffff000090816620 + uVar8 * 0x110;
      (&DAT_ffff000090816718)[uVar8 * 0x22] = lVar3;
      FUN_ffff00009077eb20(puVar6,&DAT_ffff000090803cec,uVar4 * 0x3c);
      uVar4 = (ulong)DAT_ffff000090816860;
      puVar5 = puVar9;
      if (0 < (int)DAT_ffff000090816860) {
        do {
          uVar4 = uVar4 - 1;
          puVar5[-4] = *(uint *)(&DAT_ffff000090816720 + (ulong)puVar5[-4] * 4 + uVar8 * 0x110);
          uVar1 = *(uint *)(&DAT_ffff000090816720 + (ulong)*puVar5 * 4 + uVar8 * 0x110);
          *puVar5 = uVar1;
          puVar5[-2] = uVar1 + 1;
          puVar5 = puVar5 + 0xf;
        } while (uVar4 != 0);
      }
      FUN_ffff00009077eb20(&DAT_ffff0000908166d8 + uVar8 * 0x22,&DAT_ffff000090816850,0x40);
      (&DAT_ffff0000908166f0)[uVar8 * 0x22] = puVar6;
      puVar7 = (&PTR_s_mt6379_auxadc_1_ffff000090816840)[uVar8];
      (&DAT_ffff0000908166d8)[uVar8 * 0x22] = puVar7;
      (&DAT_ffff0000908166e0)[uVar8 * 0x22] = puVar6;
      iVar2 = FUN_ffff000090736cb4(&DAT_ffff0000908166d8 + uVar8 * 0x22);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_register__s_ffff0000907faf7f,
                   s_mt6379_auxadc_init_ffff0000907f2579,0x96,puVar7);
        return;
      }
      FUN_ffff00009077e11c
                (s__s__d__Register__s_successfully_ffff0000907ebd75,
                 s_mt6379_auxadc_init_ffff0000907f2579,0x99,puVar7);
      uVar8 = uVar8 + 1;
      uVar4 = FUN_ffff0000907414e4();
      puVar9 = puVar9 + 0x44;
    } while (uVar8 < (uVar4 & 0xffffffff));
  }
  FUN_ffff00009077e11c
            (s__s__d__LK_init_done__ffff0000907f3de7,s_mt6379_auxadc_init_ffff0000907f2579,0x9c);
  return;
}


