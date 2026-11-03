// ffff000090784c6c  size=524  FUN_ffff000090784c6c

void FUN_ffff000090784c6c(undefined8 param_1)

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
  iVar2 = FUN_ffff00009074a0f8(&local_40,0x20);
  if (iVar2 == 0) {
    local_64 = 0;
    lVar3 = FUN_ffff00009076daec(param_1,0,s_model_ffff000090802dcc,&local_64);
    if (lVar3 != 0) {
      iVar2 = FUN_ffff00009077ef88(&local_40);
      iVar2 = FUN_ffff00009076e40c(param_1,0,s_model_ffff000090802dcc,&local_40,iVar2 + 1);
      if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_devinfo_ffff0000907e1f2a,0xb9,&DAT_ffff0000907d24c9);
      }
      lVar3 = FUN_ffff00009076daec(param_1,0,s_model_ffff000090802dcc,&local_64);
      if (lVar3 != 0) {
        uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
        uVar4 = FUN_ffff00009076d10c(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
        FUN_ffff00009077de54(s__s__s_ffff0000907f53ed,uVar4,lVar3 + 0xc);
      }
      iVar2 = FUN_ffff00009074a1a4(&local_60,0x20);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff00009077ef88(&local_60);
        iVar2 = FUN_ffff00009076e40c
                          (param_1,0,s_model_external_name_ffff0000907eec30,&local_60,iVar2 + 1);
        if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_devinfo_ffff0000907e1f2a,0xc5,&DAT_ffff0000907d24c9)
          ;
        }
        lVar3 = FUN_ffff00009076daec(param_1,0,s_model_external_name_ffff0000907eec30,&local_64);
        if (lVar3 != 0) {
          uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
          uVar4 = FUN_ffff00009076d10c(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
          FUN_ffff00009077de54(s__s__s_ffff0000907f53ed,uVar4,lVar3 + 0xc);
        }
        iVar2 = FUN_ffff00009077ef88(&local_60);
        iVar2 = FUN_ffff00009076e40c
                          (param_1,0,s_model_part_name_ffff0000907e6ba2,&local_60,iVar2 + 1);
        if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_common_devinfo_ffff0000907e1f2a,0xd0,&DAT_ffff0000907d24c9)
          ;
        }
        lVar3 = FUN_ffff00009076daec(param_1,0,s_model_part_name_ffff0000907e6ba2,&local_64);
        if (lVar3 != 0) {
          uVar1 = (*(uint *)(lVar3 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 8) & 0xff00ff) << 8;
          uVar4 = FUN_ffff00009076d10c(param_1,uVar1 >> 0x10 | uVar1 << 0x10);
          FUN_ffff00009077de54(s__s__s_ffff0000907f53ed,uVar4,lVar3 + 0xc);
        }
      }
    }
  }
  return;
}


