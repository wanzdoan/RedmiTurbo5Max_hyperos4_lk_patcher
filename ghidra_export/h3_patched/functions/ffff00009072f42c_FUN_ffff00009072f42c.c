// ffff00009072f42c  size=488  FUN_ffff00009072f42c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072f42c(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  undefined4 *puVar4;
  undefined4 local_44;
  
  FUN_ffff00009072dc7c();
  iVar1 = FUN_ffff00009072e180();
  if (iVar1 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_MD_not_EN__power_of_ffff0000907df841,
               s_platform_load_modem_ffff0000907d0ff5,0x2c6);
  }
  else {
    iVar1 = FUN_ffff000090730110();
    if ((-1 < iVar1) && (iVar1 = FUN_ffff00009073203c(), -1 < iVar1)) {
      iVar1 = FUN_ffff000090798664(param_2);
      iVar2 = FUN_ffff00009072f614();
      if (iVar1 == 0) {
        if ((-1 < iVar2) && (iVar1 = FUN_ffff00009072c030(param_1,param_2), -1 < iVar1)) {
          FUN_ffff000090798210();
          if (_DAT_ffff00009086d780 != 0) {
            uVar3 = 0;
            puVar4 = (undefined4 *)&DAT_ffff00009086d790;
            do {
              FUN_ffff00009077de54
                        (s__s__d___CCCI_Free_0x_llx_with_si_ffff0000907f6190,
                         s_free_not_used_memory_ffff0000907e47a2,0x165,*(undefined8 *)(puVar4 + -2),
                         *puVar4);
              FUN_ffff00009072dba4(*(undefined8 *)(puVar4 + -2),*puVar4);
              uVar3 = uVar3 + 1;
              puVar4 = puVar4 + 4;
            } while (uVar3 < _DAT_ffff00009086d780);
          }
          FUN_ffff000090731ee4();
          local_44 = 1;
          FUN_ffff00009077de54
                    (s__s__d___CCCI_________ffff0000907d1061,
                     s_load_modem_image_compatible_ffff0000907f61b9,0x48);
          FUN_ffff0000907320d8(0);
          iVar1 = FUN_ffff0000907320fc(s_hdr_count_ffff0000907da9ce,&local_44,4);
          if (iVar1 < 0) {
            FUN_ffff00009077de54
                      (s__s__d___CCCI_insert_hdr_count_fa_ffff0000907e2bd5,
                       s_load_modem_image_compatible_ffff0000907f61b9,0x4c);
          }
          iVar1 = FUN_ffff0000907320fc(s_hdr_tbl_inf_ffff0000907d6087,&DAT_ffff00009086d720,0x18);
          if (iVar1 < 0) {
            FUN_ffff00009077de54
                      (s__s__d___CCCI_insert_hdr_tbl_inf_f_ffff0000907d1077,
                       s_load_modem_image_compatible_ffff0000907f61b9,0x51);
          }
          FUN_ffff00009077de54
                    (s__s__d___CCCI__________ffff0000907feeaf,
                     s_load_modem_image_compatible_ffff0000907f61b9,0x52);
          FUN_ffff000090730098();
          FUN_ffff0000907320d8(0);
          FUN_ffff00009072dc70(param_1);
          FUN_ffff00009072dc24();
          FUN_ffff000090797d74();
          FUN_ffff000090731f98();
          return 0;
        }
        FUN_ffff0000907320c4(5);
        FUN_ffff000090730098();
      }
    }
  }
  FUN_ffff00009072dc70(0);
  FUN_ffff000090797c0c();
  return 0;
}


