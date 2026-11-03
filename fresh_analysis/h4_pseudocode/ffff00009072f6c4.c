
undefined8 FUN_ffff00009072f6c4(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  undefined4 *puVar4;
  undefined4 local_44;
  
  FUN_ffff00009072df14();
  iVar1 = FUN_ffff00009072e418();
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_MD_not_EN__power_of_ffff0000907df8fb,
               s_platform_load_modem_ffff0000907d1032,0x2c6);
  }
  else {
    iVar1 = FUN_ffff0000907303a8();
    if ((-1 < iVar1) && (iVar1 = FUN_ffff0000907322d4(), -1 < iVar1)) {
      iVar1 = FUN_ffff000090798924(param_2);
      iVar2 = FUN_ffff00009072f8ac();
      if (iVar1 == 0) {
        if ((-1 < iVar2) && (iVar1 = FUN_ffff00009072c2c8(param_1,param_2), -1 < iVar1)) {
          FUN_ffff0000907984d0();
          if (DAT_ffff00009086d818 != 0) {
            uVar3 = 0;
            puVar4 = &DAT_ffff00009086d828;
            do {
              FUN_ffff00009077e11c
                        (s__s__d___CCCI_Free_0x_llx_with_si_ffff0000907f6331,
                         s_free_not_used_memory_ffff0000907e48bd,0x165,*(undefined8 *)(puVar4 + -2),
                         *puVar4);
              FUN_ffff00009072de3c(*(undefined8 *)(puVar4 + -2),*puVar4);
              uVar3 = uVar3 + 1;
              puVar4 = puVar4 + 4;
            } while (uVar3 < DAT_ffff00009086d818);
          }
          FUN_ffff00009073217c();
          local_44 = 1;
          FUN_ffff00009077e11c
                    (s__s__d___CCCI_________ffff0000907d109e,
                     s_load_modem_image_compatible_ffff0000907f635a,0x48);
          FUN_ffff000090732370(0);
          iVar1 = FUN_ffff000090732394(s_hdr_count_ffff0000907daaf0,&local_44,4);
          if (iVar1 < 0) {
            FUN_ffff00009077e11c
                      (s__s__d___CCCI_insert_hdr_count_fa_ffff0000907e2cbd,
                       s_load_modem_image_compatible_ffff0000907f635a,0x4c);
          }
          iVar1 = FUN_ffff000090732394(s_hdr_tbl_inf_ffff0000907d611d,&DAT_ffff00009086d7b8,0x18);
          if (iVar1 < 0) {
            FUN_ffff00009077e11c
                      (s__s__d___CCCI_insert_hdr_tbl_inf_f_ffff0000907d10b4,
                       s_load_modem_image_compatible_ffff0000907f635a,0x51);
          }
          FUN_ffff00009077e11c
                    (s__s__d___CCCI__________ffff0000907ff057,
                     s_load_modem_image_compatible_ffff0000907f635a,0x52);
          FUN_ffff000090730330();
          FUN_ffff000090732370(0);
          FUN_ffff00009072df08(param_1);
          FUN_ffff00009072debc();
          FUN_ffff000090798034();
          FUN_ffff000090732230();
          return 0;
        }
        FUN_ffff00009073235c(5);
        FUN_ffff000090730330();
      }
    }
  }
  FUN_ffff00009072df08(0);
  FUN_ffff000090797ecc();
  return 0;
}


