// ffff0000907982d4  size=444  FUN_ffff0000907982d4

void FUN_ffff0000907982d4(void)

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
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_ON__s_set_dp_ffff0000907f586b,s_md_set_dpsw_ffff0000907e3d5e,0x72
             ,s_md_set_dpsw_ffff0000907e3d5e);
  FUN_ffff00009071ebf4(0xc200040b,6,9,0,0,0,0,0,&local_40);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__POWER_ON__s_MD_DVF_ffff0000907def97,s_md_set_dpsw_ffff0000907e3d5e,0x74
             ,s_md_set_dpsw_ffff0000907e3d5e,local_28);
  local_4[0] = 0x80;
  lVar3 = FUN_ffff000090740610(1,4);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907f099f,s_md_set_dpsw_ffff0000907e3d5e,
               0x79,s_md_set_dpsw_ffff0000907e3d5e);
  }
  else {
    iVar1 = FUN_ffff0000907404b4(lVar3,0x252,local_4,1);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907fe4c3,s_md_set_dpsw_ffff0000907e3d5e,
                 0x7f,s_md_set_dpsw_ffff0000907e3d5e,iVar1);
    }
    uVar2 = FUN_ffff00009074046c(lVar3,0x252,local_8,1);
    if ((int)uVar2 < 0) {
      pcVar4 = s__s__d___CCCI__POWER_ON__s_fail_t_ffff0000907fe4fd;
      uVar5 = 0x82;
    }
    else {
      uVar2 = (uint)local_8[0];
      pcVar4 = s__s__d___CCCI__POWER_ON__s_get_Vm_ffff0000907ed5d5;
      uVar5 = 0x84;
    }
    FUN_ffff00009077e11c
              (pcVar4,s_md_set_dpsw_ffff0000907e3d5e,uVar5,s_md_set_dpsw_ffff0000907e3d5e,uVar2);
    FUN_ffff00009077cea0(0xf);
    FUN_ffff00009071ebf4(0xc200040b,6,9,1,0,0,0,0,&local_40);
    FUN_ffff00009077e11c
              (s__s__d___CCCI__POWER_ON__s_MD_DVF_ffff0000907def97,s_md_set_dpsw_ffff0000907e3d5e,
               0x88,s_md_set_dpsw_ffff0000907e3d5e,local_28);
  }
  return;
}


