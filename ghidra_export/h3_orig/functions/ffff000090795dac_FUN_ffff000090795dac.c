// ffff000090795dac  size=768  FUN_ffff000090795dac

int FUN_ffff000090795dac(undefined1 param_1,uint param_2,long param_3,ulong param_4,uint *param_5)

{
  ushort uVar1;
  undefined4 uVar2;
  long lVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  char *pcVar7;
  uint uVar8;
  undefined4 local_94;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  uint6 local_78;
  undefined2 uStack_72;
  undefined4 local_70;
  undefined4 uStack_6c;
  long local_68;
  
  local_68 = -0xffff6f6f5b30;
  lVar5 = FUN_ffff000090792e88(0);
  if ((param_4 & 0xff) == 0) {
    _local_78 = 0;
    local_70 = 0;
    uStack_6c = 0;
    local_88 = 0;
    local_80 = 0;
    local_90 = 0;
    local_94 = 0;
    FUN_ffff0000907175f4(lVar5 + 0x270,0xffffffff);
    uVar6 = FUN_ffff00009078fca8(lVar5,&local_94);
    if ((uVar6 & 1) == 0) {
      iVar4 = -6;
    }
    else {
      FUN_ffff000090795504();
      if ((uint)param_4 < 0x100) {
        iVar4 = 0;
      }
      else {
        uVar8 = (uint)(param_4 >> 8) & 0xffffff;
        if (uVar8 < 2) {
          uVar8 = 1;
        }
        uVar6 = (ulong)uVar8;
        do {
          uVar2 = local_94;
          FUN_ffff00009077eb88((long)&local_88 + 5,0,0x10);
          lVar3 = local_68;
          local_80 = 0x20000000001;
          local_90 = CONCAT44(uVar2,0xc4);
          local_78 = (uint6)_local_78 & 0xffffffffff00;
          local_70 = 0x200;
          _local_78 = CONCAT26(0xc,local_78);
          local_88 = CONCAT17(param_1,0xecb50000000001);
          if (local_68 == 0) {
            FUN_ffff00009077de54
                      (s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd472,
                       s_ufshcd_rpmb_read_data_req_prepar_ffff0000907e3c33);
            pcVar7 = s___s___RPMB_Fail_in_prepare_frame_ffff0000907d6ebf;
            iVar4 = -5;
LAB_ffff000090796028:
            FUN_ffff00009077de54(pcVar7,s_ufs_rpmb_read_ffff0000907d3a19,iVar4);
            iVar4 = -6;
            goto LAB_ffff000090796070;
          }
          FUN_ffff00009077eb88(local_68,0,0x200);
          *(undefined2 *)(lVar3 + 0x1fa) = 0x100;
          *(ushort *)(lVar3 + 0x1f8) =
               (ushort)(param_2 >> 8) & 0xff | (ushort)((param_2 & 0xff00ff) << 8);
          *(undefined2 *)(lVar3 + 0x1fe) = 0x400;
          FUN_ffff00009077e858(lVar3 + 0x1e4,0xffff00009090a700,0x10);
          iVar4 = FUN_ffff000090790d5c(lVar5,&local_90);
          uVar2 = local_94;
          if (iVar4 != 0) {
            pcVar7 = s___s___RPMB_Fail_in_send_read_fra_ffff0000907f2046;
            goto LAB_ffff000090796028;
          }
          FUN_ffff00009077eb88((long)&local_88 + 5,0,0x10);
          local_90 = CONCAT44(uVar2,0xc4);
          local_80 = 0x20000000001;
          local_78 = (uint6)_local_78 & 0xffffffffff00;
          local_70 = 0x200;
          _local_78 = CONCAT26(0xc,local_78);
          local_88 = CONCAT17(param_1,0xeca20000000002);
          iVar4 = FUN_ffff000090790d5c(lVar5,&local_90);
          if (iVar4 != 0) {
            pcVar7 = s___s___RPMB_Fail_in_get_read_resp_ffff0000907f5653;
            goto LAB_ffff000090796028;
          }
          if ((param_5 != (uint *)0x0) &&
             (uVar1 = *(ushort *)(local_68 + 0x1fc),
             *param_5 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8, uVar1 != 0)) {
            FUN_ffff00009077de54
                      (s___s___result____d_ffff0000907f7325,s_ufs_rpmb_read_ffff0000907d3a19);
            iVar4 = -2;
            goto LAB_ffff000090796070;
          }
          iVar4 = FUN_ffff0000907960ac(&local_90,0xffff00009090a700,param_3,0x400);
          if (iVar4 != 0) {
            FUN_ffff00009077de54
                      (s___s___RPMB_Data_Read_Resp_Check_F_ffff0000907e6e08,
                       s_ufs_rpmb_read_ffff0000907d3a19,iVar4);
            goto LAB_ffff000090796070;
          }
          param_2 = param_2 + 1;
          param_3 = param_3 + 0x100;
          uVar6 = uVar6 - 1;
        } while (uVar6 != 0);
        iVar4 = 0;
      }
LAB_ffff000090796070:
      FUN_ffff00009078fd78(lVar5,local_94);
    }
    FUN_ffff00009071768c(lVar5 + 0x270);
  }
  else {
    FUN_ffff00009077de54
              (s___s___RPMB_read_write_need_256_y_ffff0000907f72fa,s_ufs_rpmb_read_ffff0000907d3a19)
    ;
    iVar4 = -6;
  }
  return iVar4;
}


