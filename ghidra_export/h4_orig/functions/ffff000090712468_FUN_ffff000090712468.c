// ffff000090712468  size=440  FUN_ffff000090712468

void FUN_ffff000090712468(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  char *pcVar5;
  
  iVar2 = FUN_ffff00009076da24(param_1,s__firmware_android_ffff000090800544);
  if (iVar2 < 0) {
    iVar2 = FUN_ffff00009076da24(param_1,s__firmware_ffff0000907f416c);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076eb40(param_1,0,s_firmware_ffff0000907d0b2e);
      if (iVar2 < 0) {
        pcVar5 = s_Warning__can_t_add_firmware_node_ffff0000907d2627;
        goto LAB_ffff0000907125c4;
      }
    }
    iVar2 = FUN_ffff00009076eb40(param_1,iVar2,s_android_ffff000090801fb8);
    if (iVar2 < 0) {
      pcVar5 = s_Warning__can_t_add_firmware_andr_ffff0000907f0dd9;
LAB_ffff0000907125c4:
      FUN_ffff00009077e11c(pcVar5);
      return;
    }
  }
  iVar3 = FUN_ffff00009077f250(s_android_firmware_ffff000090801fc0);
  iVar3 = FUN_ffff00009076e6d4
                    (param_1,iVar2,s_compatible_ffff0000907d74bf,s_android_firmware_ffff000090801fc0
                     ,iVar3 + 1);
  if (iVar3 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_fdt_para_c_ffff0000907e5bef,0x59,&DAT_ffff0000907d2506);
  }
  uVar4 = FUN_ffff00009070af2c();
  FUN_ffff00009070af2c();
  iVar3 = FUN_ffff00009077f250();
  iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_serialno_ffff0000907d24f8,uVar4,iVar3 + 1);
  if (iVar3 == 0) {
    cVar1 = FUN_ffff000090710bc0();
    if ((byte)(cVar1 - 1U) < 8) {
      pcVar5 = &DAT_ffff000090804498 +
               *(int *)(&DAT_ffff000090804498 + (long)(char)(cVar1 - 1U) * 4);
    }
    else {
      pcVar5 = s_normal_ffff0000907f9409;
    }
    iVar3 = FUN_ffff00009077f250(pcVar5);
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,&DAT_ffff0000907da621,pcVar5,iVar3 + 1);
    if (iVar2 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc(s_app_mt_boot_fdt_para_c_ffff0000907e5bef,0x76,&DAT_ffff0000907d2506);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc(s_app_mt_boot_fdt_para_c_ffff0000907e5bef,0x5e,&DAT_ffff0000907d2506);
}


