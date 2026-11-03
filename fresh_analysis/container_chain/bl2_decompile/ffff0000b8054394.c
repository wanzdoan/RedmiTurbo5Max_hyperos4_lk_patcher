
void FUN_ffff0000b8054394(long param_1,uint *param_2)

{
  short sVar1;
  bool bVar2;
  bool bVar3;
  undefined4 uVar4;
  undefined8 uVar5;
  code *pcVar6;
  uint uVar7;
  int iVar8;
  undefined4 uVar9;
  uint local_34;
  
  sVar1 = *(short *)(param_1 + 0x88);
  if (sVar1 == 0) {
    uVar7 = 0;
    bVar2 = false;
    bVar3 = false;
    uVar9 = 0;
    iVar8 = 0;
  }
  else {
    uVar7 = param_2[1];
    if (*param_2 <= param_2[1]) {
      uVar7 = *param_2;
    }
    if (4 < uVar7) {
      uVar7 = 5;
    }
    FUN_ffff0000b804c33c(s__UFS__info__RX_HS_G_d__setting_i_ffff0000b8089684,uVar7,5);
    FUN_ffff0000b804c33c(s__UFS__info__TX_HS_G_d__setting_i_ffff0000b8085b87,uVar7,5);
    iVar8 = 2;
    bVar2 = param_2[2] != 0;
    bVar3 = param_2[3] != 0;
    FUN_ffff0000b804c33c(s__UFS__info__HS_G_d__d___d_lane_ffff0000b808a33b,uVar7,2,bVar2);
    (**(code **)(param_1 + 0x248))(param_1,0x15b00000,0x1fff);
    (**(code **)(param_1 + 0x248))(param_1,0x15b10000,0xffff);
    (**(code **)(param_1 + 0x248))(param_1,0x15b20000,0x7fff);
    (**(code **)(param_1 + 0x248))(param_1,0x15b30000,0x1fff);
    (**(code **)(param_1 + 0x248))(param_1,0x15b40000,0xffff);
    (**(code **)(param_1 + 0x248))(param_1,0x15b50000,0x7fff);
    (**(code **)(param_1 + 0x248))(param_1,0xd0410000,0x1fff);
    (**(code **)(param_1 + 0x248))(param_1,0xd0420000,0xffff);
    (**(code **)(param_1 + 0x248))(param_1,0xd0430000,0x7fff);
    uVar9 = 1;
  }
  local_34 = 0;
  (**(code **)(param_1 + 0x238))(param_1,0x15a90000,&local_34);
  FUN_ffff0000b804c33c(s__UFS__unipro_info__local__d_ffff0000b8085b14,local_34);
  if (5 < local_34) {
    if ((iVar8 != *(int *)(param_1 + 0x70)) && (sVar1 != 0 || 3 < uVar7)) {
      (**(code **)(param_1 + 0x248))(param_1,0x15d40000,3);
      uVar4 = FUN_ffff0000b8054078(param_1,4,4,1,1,iVar8,bVar3,bVar2);
      FUN_ffff0000b804c33c(s__UFS__Power_mode_change_to_HSG1_F_ffff0000b80a1575,uVar4);
    }
  }
  if (sVar1 == 0) {
    pcVar6 = *(code **)(param_1 + 0x248);
  }
  else {
    pcVar6 = *(code **)(param_1 + 0x248);
    if (3 < uVar7) {
      uVar5 = 1;
      goto LAB_ffff0000b80545cc;
    }
  }
  uVar5 = 3;
LAB_ffff0000b80545cc:
  (*pcVar6)(param_1,0x15d40000,uVar5);
  FUN_ffff0000b8054078(param_1,uVar9,uVar9,uVar7,uVar7,iVar8,bVar3,bVar2);
  return;
}


