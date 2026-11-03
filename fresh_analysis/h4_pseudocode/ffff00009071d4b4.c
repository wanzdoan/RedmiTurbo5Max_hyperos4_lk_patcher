
int FUN_ffff00009071d4b4(long param_1)

{
  int iVar1;
  long lVar2;
  long local_40;
  long lStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (param_1 == 0) {
    return -7;
  }
  iVar1 = FUN_ffff00009076da24(param_1,s__mvpu_ffff0000907f7df4);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__s__mvpu_not_found__so_searching_ffff0000907e0d92,s_mvpu_prop_off_ffff0000907f5fbf)
    ;
    iVar1 = FUN_ffff00009076da24(param_1,s_soc_mvpu_ffff0000907dc3d7);
    if (-1 < iVar1) goto LAB_ffff00009071d508;
    FUN_ffff00009077e11c
              (s__s__mvpu_not_found_in_soc_ffff0000907d40ab,s_mvpu_prop_off_ffff0000907f5fbf);
    iVar1 = FUN_ffff00009071c708();
    if (iVar1 == 0) goto LAB_ffff00009071d5ac;
LAB_ffff00009071d510:
    lVar2 = FUN_ffff00009071a09c
                      (0x200000,0x200000,0x240000000,0x340000000,0,0,
                       s_apu_sdsp_mvpugzshmalgo_ffff00009080077d);
    if (lVar2 != 0) goto LAB_ffff00009071d544;
LAB_ffff00009071d5d4:
    FUN_ffff00009077e11c(s_apu_sdsp_mvpugzshmalgo_mblock_al_ffff0000907e0db9);
    iVar1 = -5;
  }
  else {
LAB_ffff00009071d508:
    iVar1 = FUN_ffff00009071c708();
    if (iVar1 != 0) goto LAB_ffff00009071d510;
LAB_ffff00009071d5ac:
    lVar2 = FUN_ffff0000907198dc
                      (0x200000,0x200000,0x90000000,0,0,s_apu_sdsp_mvpugzshmalgo_ffff00009080077d);
    if (lVar2 == 0) goto LAB_ffff00009071d5d4;
LAB_ffff00009071d544:
    FUN_ffff00009077e11c(s_apu_sdsp_mvpugzshmalgo_mblock_pa_ffff0000907dc3e0,lVar2,0x200000);
    lStack_38 = lVar2 + 0x200000;
    uStack_28 = 0;
    local_30 = 0x15;
    local_40 = lVar2;
    iVar1 = FUN_ffff00009078b0fc(&local_40);
    if (iVar1 == 0) goto LAB_ffff00009071d5fc;
    FUN_ffff00009071a7f0(lVar2);
  }
  FUN_ffff00009077e11c
            (s__s__prepare_sdsp_shm_algo_fail___ffff0000907d763d,s_apu_load_mvpu_ffff0000907d7661,
             iVar1);
LAB_ffff00009071d5fc:
  iVar1 = FUN_ffff00009071c708();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000907198dc
                      (0x400000,0x200000,0x90000000,0,0,s_apu_sdsp_mvpugzshmdata_ffff0000907f0f87);
  }
  else {
    lVar2 = FUN_ffff00009071a09c
                      (0x400000,0x200000,0x240000000,0x340000000,0,0,
                       s_apu_sdsp_mvpugzshmdata_ffff0000907f0f87);
  }
  if (lVar2 == 0) {
    FUN_ffff00009077e11c(s_apu_sdsp_mvpugzshmdata_mblock_al_ffff0000907fb411);
    iVar1 = -5;
  }
  else {
    FUN_ffff00009077e11c(s_apu_sdsp_mvpugzshmdata_mblock_pa_ffff0000907cf37c,lVar2,0x400000);
    lStack_38 = lVar2 + 0x400000;
    uStack_28 = 0;
    local_30 = 0x16;
    local_40 = lVar2;
    iVar1 = FUN_ffff00009078b0fc(&local_40);
    if (iVar1 == 0) {
      return 0;
    }
    FUN_ffff00009071a7f0(lVar2);
  }
  FUN_ffff00009077e11c
            (s__s__prepare_sdsp_shm_algo_fail___ffff0000907d763d,s_apu_load_mvpu_ffff0000907d7661,
             iVar1);
  return iVar1;
}


