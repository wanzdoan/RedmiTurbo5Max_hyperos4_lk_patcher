// ffff000090712384  size=440  FUN_ffff000090712384

void FUN_ffff000090712384(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  char *pcVar5;
  
  iVar2 = FUN_ffff00009076d75c(param_1,s__firmware_android_ffff000090800368);
  if (iVar2 < 0) {
    iVar2 = FUN_ffff00009076d75c(param_1,s__firmware_ffff0000907f3fc9);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076e878(param_1,0,s_firmware_ffff0000907d0b17);
      if (iVar2 < 0) {
        pcVar5 = s_Warning__can_t_add_firmware_node_ffff0000907d25ea;
        goto LAB_ffff0000907124e0;
      }
    }
    iVar2 = FUN_ffff00009076e878(param_1,iVar2,s_android_ffff000090801dd2);
    if (iVar2 < 0) {
      pcVar5 = s_Warning__can_t_add_firmware_andr_ffff0000907f0c51;
LAB_ffff0000907124e0:
      FUN_ffff00009077de54(pcVar5);
      return;
    }
  }
  iVar3 = FUN_ffff00009077ef88(s_android_firmware_ffff000090801dda);
  iVar3 = FUN_ffff00009076e40c
                    (param_1,iVar2,s_compatible_ffff0000907d7429,s_android_firmware_ffff000090801dda
                     ,iVar3 + 1);
  if (iVar3 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_fdt_para_c_ffff0000907e5ac4,0x59,&DAT_ffff0000907d24c9);
  }
  uVar4 = FUN_ffff00009070aeb4();
  FUN_ffff00009070aeb4();
  iVar3 = FUN_ffff00009077ef88();
  iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_serialno_ffff0000907d24bb,uVar4,iVar3 + 1);
  if (iVar3 == 0) {
    cVar1 = FUN_ffff000090710b48();
    if ((byte)(cVar1 - 1U) < 8) {
      pcVar5 = &DAT_ffff000090804270 +
               *(int *)(&DAT_ffff000090804270 + (long)(char)(cVar1 - 1U) * 4);
    }
    else {
      pcVar5 = s_normal_ffff0000907f926d;
    }
    iVar3 = FUN_ffff00009077ef88(pcVar5);
    iVar2 = FUN_ffff00009076e40c(param_1,iVar2,&DAT_ffff0000907da53c,pcVar5,iVar3 + 1);
    if (iVar2 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_fdt_para_c_ffff0000907e5ac4,0x76,&DAT_ffff0000907d24c9);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04(s_app_mt_boot_fdt_para_c_ffff0000907e5ac4,0x5e,&DAT_ffff0000907d24c9);
}


