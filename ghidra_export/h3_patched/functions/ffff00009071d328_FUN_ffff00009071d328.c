// ffff00009071d328  size=560  FUN_ffff00009071d328

int FUN_ffff00009071d328(long param_1)

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
  iVar1 = FUN_ffff00009076d75c(param_1,s__mvpu_ffff0000907f7c58);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__mvpu_not_found__so_searching_ffff0000907e0cd8,s_mvpu_prop_off_ffff0000907f5e1e)
    ;
    iVar1 = FUN_ffff00009076d75c(param_1,s_soc_mvpu_ffff0000907dc2ea);
    if (-1 < iVar1) goto LAB_ffff00009071d37c;
    FUN_ffff00009077de54
              (s__s__mvpu_not_found_in_soc_ffff0000907d406e,s_mvpu_prop_off_ffff0000907f5e1e);
    iVar1 = FUN_ffff00009071c57c();
    if (iVar1 == 0) goto LAB_ffff00009071d420;
LAB_ffff00009071d384:
    lVar2 = FUN_ffff000090719f58
                      (0x200000,0x200000,0x240000000,0x340000000,0,0,
                       s_apu_sdsp_mvpugzshmalgo_ffff0000908005a1);
    if (lVar2 != 0) goto LAB_ffff00009071d3b8;
LAB_ffff00009071d448:
    FUN_ffff00009077de54(s_apu_sdsp_mvpugzshmalgo_mblock_al_ffff0000907e0cff);
    iVar1 = -5;
  }
  else {
LAB_ffff00009071d37c:
    iVar1 = FUN_ffff00009071c57c();
    if (iVar1 != 0) goto LAB_ffff00009071d384;
LAB_ffff00009071d420:
    lVar2 = FUN_ffff000090719798
                      (0x200000,0x200000,0x90000000,0,0,s_apu_sdsp_mvpugzshmalgo_ffff0000908005a1);
    if (lVar2 == 0) goto LAB_ffff00009071d448;
LAB_ffff00009071d3b8:
    FUN_ffff00009077de54(s_apu_sdsp_mvpugzshmalgo_mblock_pa_ffff0000907dc2f3,lVar2,0x200000);
    lStack_38 = lVar2 + 0x200000;
    uStack_28 = 0;
    local_30 = 0x15;
    local_40 = lVar2;
    iVar1 = FUN_ffff00009078ae3c(&local_40);
    if (iVar1 == 0) goto LAB_ffff00009071d470;
    FUN_ffff00009071a6ac(lVar2);
  }
  FUN_ffff00009077de54
            (s__s__prepare_sdsp_shm_algo_fail___ffff0000907d75a7,s_apu_load_mvpu_ffff0000907d75cb,
             iVar1);
LAB_ffff00009071d470:
  iVar1 = FUN_ffff00009071c57c();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff000090719798
                      (0x400000,0x200000,0x90000000,0,0,s_apu_sdsp_mvpugzshmdata_ffff0000907f0dff);
  }
  else {
    lVar2 = FUN_ffff000090719f58
                      (0x400000,0x200000,0x240000000,0x340000000,0,0,
                       s_apu_sdsp_mvpugzshmdata_ffff0000907f0dff);
  }
  if (lVar2 == 0) {
    FUN_ffff00009077de54(s_apu_sdsp_mvpugzshmdata_mblock_al_ffff0000907fb275);
    iVar1 = -5;
  }
  else {
    FUN_ffff00009077de54(s_apu_sdsp_mvpugzshmdata_mblock_pa_ffff0000907cf35d,lVar2,0x400000);
    lStack_38 = lVar2 + 0x400000;
    uStack_28 = 0;
    local_30 = 0x16;
    local_40 = lVar2;
    iVar1 = FUN_ffff00009078ae3c(&local_40);
    if (iVar1 == 0) {
      return 0;
    }
    FUN_ffff00009071a6ac(lVar2);
  }
  FUN_ffff00009077de54
            (s__s__prepare_sdsp_shm_algo_fail___ffff0000907d75a7,s_apu_load_mvpu_ffff0000907d75cb,
             iVar1);
  return iVar1;
}


