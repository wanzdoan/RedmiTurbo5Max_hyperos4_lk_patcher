// ffff000090784f34  size=524  FUN_ffff000090784f34

void FUN_ffff000090784f34(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined4 local_64;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  iVar2 = FUN_ffff00009074a390(&local_40,0x20);
  if (iVar2 == 0) {
    local_64 = 0;
    lVar3 = FUN_ffff00009076ddb4(param_1,0,s_model_ffff000090802fd0,&local_64);
    if (lVar3 != 0) {
      iVar2 = FUN_ffff00009077f250(&local_40);
      iVar2 = FUN_ffff00009076e6d4(param_1,0,s_model_ffff000090802fd0,&local_40,iVar2 + 1);
      if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_devinfo_ffff0000907e2012,0xb9,&DAT_ffff0000907d2506);
      }
      lVar3 = FUN_ffff00009076ddb4(param_1,0,s_model_ffff000090802fd0,&local_64);
      if (lVar3 != 0) {
        uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
        uVar4 = FUN_ffff00009076d3d4(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
        FUN_ffff00009077e11c(s__s__s_ffff0000907f5571,uVar4,lVar3 + 0xc);
      }
      iVar2 = FUN_ffff00009074a43c(&local_60,0x20);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff00009077f250(&local_60);
        iVar2 = FUN_ffff00009076e6d4
                          (param_1,0,s_model_external_name_ffff0000907eedb8,&local_60,iVar2 + 1);
        if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_devinfo_ffff0000907e2012,0xc5,&DAT_ffff0000907d2506)
          ;
        }
        lVar3 = FUN_ffff00009076ddb4(param_1,0,s_model_external_name_ffff0000907eedb8,&local_64);
        if (lVar3 != 0) {
          uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
          uVar4 = FUN_ffff00009076d3d4(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
          FUN_ffff00009077e11c(s__s__s_ffff0000907f5571,uVar4,lVar3 + 0xc);
        }
        iVar2 = FUN_ffff00009077f250(&local_60);
        iVar2 = FUN_ffff00009076e6d4
                          (param_1,0,s_model_part_name_ffff0000907e6ccd,&local_60,iVar2 + 1);
        if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_common_devinfo_ffff0000907e2012,0xd0,&DAT_ffff0000907d2506)
          ;
        }
        lVar3 = FUN_ffff00009076ddb4(param_1,0,s_model_part_name_ffff0000907e6ccd,&local_64);
        if (lVar3 != 0) {
          uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
          uVar4 = FUN_ffff00009076d3d4(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
          FUN_ffff00009077e11c(s__s__s_ffff0000907f5571,uVar4,lVar3 + 0xc);
        }
      }
    }
  }
  return;
}


