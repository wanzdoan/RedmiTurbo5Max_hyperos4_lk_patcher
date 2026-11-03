// ffff000090798014  size=444  FUN_ffff000090798014

void FUN_ffff000090798014(void)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  char *pcVar4;
  undefined8 uVar5;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 local_28;
  byte local_8 [4];
  undefined1 local_4 [4];
  
  local_8[0] = 0;
  local_30 = 0;
  local_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_ON__s_set_dp_ffff0000907f56fe,s_md_set_dpsw_ffff0000907e3c8d,0x72
             ,s_md_set_dpsw_ffff0000907e3c8d);
  FUN_ffff00009071ea6c(0xc200040b,6,9,0,0,0,0,0,&local_40);
  FUN_ffff00009077de54
            (s__s__d___CCCI__POWER_ON__s_MD_DVF_ffff0000907deef4,s_md_set_dpsw_ffff0000907e3c8d,0x74
             ,s_md_set_dpsw_ffff0000907e3c8d,local_28);
  local_4[0] = 0x80;
  lVar3 = FUN_ffff000090740378(1,4);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907f082e,s_md_set_dpsw_ffff0000907e3c8d,
               0x79,s_md_set_dpsw_ffff0000907e3c8d);
  }
  else {
    iVar1 = FUN_ffff00009074021c(lVar3,0x252,local_4,1);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907fe31b,s_md_set_dpsw_ffff0000907e3c8d,
                 0x7f,s_md_set_dpsw_ffff0000907e3c8d,iVar1);
    }
    uVar2 = FUN_ffff0000907401d4(lVar3,0x252,local_8,1);
    if ((int)uVar2 < 0) {
      pcVar4 = s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907fe355;
      uVar5 = 0x82;
    }
    else {
      uVar2 = (uint)local_8[0];
      pcVar4 = s__s__d___CCCI__POWER_ON__s_get_Vm_ffff0000907ed452;
      uVar5 = 0x84;
    }
    FUN_ffff00009077de54
              (pcVar4,s_md_set_dpsw_ffff0000907e3c8d,uVar5,s_md_set_dpsw_ffff0000907e3c8d,uVar2);
    FUN_ffff00009077cbd8(0xf);
    FUN_ffff00009071ea6c(0xc200040b,6,9,1,0,0,0,0,&local_40);
    FUN_ffff00009077de54
              (s__s__d___CCCI__POWER_ON__s_MD_DVF_ffff0000907deef4,s_md_set_dpsw_ffff0000907e3c8d,
               0x88,s_md_set_dpsw_ffff0000907e3c8d,local_28);
  }
  return;
}


