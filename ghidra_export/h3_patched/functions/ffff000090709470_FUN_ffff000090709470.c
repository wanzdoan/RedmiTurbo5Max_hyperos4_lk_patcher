// ffff000090709470  size=304  FUN_ffff000090709470

void FUN_ffff000090709470(uint param_1,uint param_2,uint param_3)

{
  uint uVar1;
  byte bVar2;
  undefined *puVar3;
  undefined *puVar4;
  uint uVar5;
  uint *puVar6;
  uint *puVar7;
  uint *puVar8;
  uint *puVar9;
  
  puVar4 = PTR_LAB_ffff000090839870;
  puVar3 = PTR_DAT_ffff000090839868;
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_top_init_c_ffff0000907f797a,0x23,s_start_level_>_0_ffff0000907e72a9);
  }
  if (PTR_LAB_ffff000090839870 != PTR_DAT_ffff000090839868) {
    puVar6 = (uint *)0x0;
    uVar5 = param_2 - 1;
    do {
      bVar2 = 0;
      puVar7 = (uint *)puVar4;
      puVar9 = (uint *)0x0;
      do {
        bVar2 = puVar7 == puVar6 | bVar2;
        puVar8 = puVar9;
        if (((((((puVar7[1] & param_1) != 0) && (uVar1 = *puVar7, uVar1 <= param_3)) &&
              (uVar5 <= uVar1)) && ((puVar9 == (uint *)0x0 || (uVar1 < *puVar9)))) &&
            ((uVar1 < param_2 || (puVar8 = puVar7, uVar1 <= uVar5)))) &&
           (((puVar8 = puVar9, uVar1 == uVar5 && (puVar7 != puVar6)) && (!(bool)(bVar2 ^ 1)))))
        goto LAB_ffff000090709554;
        puVar7 = puVar7 + 6;
        puVar9 = puVar8;
      } while (puVar7 != (uint *)puVar3);
      puVar7 = puVar8;
      if (puVar8 == (uint *)0x0) {
        return;
      }
LAB_ffff000090709554:
      puVar6 = puVar7;
      (**(code **)(puVar6 + 2))(*puVar6);
      uVar5 = *puVar6;
    } while (puVar4 != puVar3);
  }
  return;
}


