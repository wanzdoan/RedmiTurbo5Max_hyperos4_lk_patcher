
int FUN_ffff000090741770(undefined8 param_1,uint param_2,long param_3,long param_4)

{
  long lVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  
  if (((DAT_ffff00009086f460 == 0) &&
      (DAT_ffff00009086f460 = FUN_ffff000090740610(1,0xe), DAT_ffff00009086f460 == 0)) &&
     (FUN_ffff00009077e11c
                (s__s__d__Failed_to_get_MT6379_SPMI_ffff0000907d9459,
                 s_get_mt6379_sdev_ffff0000907d4841,0x1f), DAT_ffff00009086f460 == 0)) {
    return -0x13;
  }
  lVar1 = DAT_ffff00009086f460;
  iVar3 = -7;
  if ((param_3 != 0) && (param_4 != 0)) {
    if ((param_2 < 0xe00) && ((param_4 + (ulong)param_2) - 1 < 0xe00)) {
      iVar4 = (int)param_4;
      iVar3 = iVar4;
      if (iVar4 < 0) {
        iVar3 = iVar4 + 1;
      }
      if (1 < iVar4) {
        iVar3 = iVar3 >> 1;
        uVar5 = ~param_2 >> 1;
        do {
          iVar2 = FUN_ffff0000907404b4(lVar1,param_2,param_3,2);
          FUN_ffff000090741454(param_2,param_3,2,iVar2);
          if (iVar2 != 0) {
            return iVar2;
          }
          if (uVar5 == 0) {
            FUN_ffff00009077e11c
                      (s__s__d__the_next_register_address_ffff0000907eafa2,
                       s_mt6379_reg_bulk_write_ffff0000907ec820,0x66);
            goto LAB_ffff0000907418c8;
          }
          param_2 = param_2 + 2;
          param_3 = param_3 + 2;
          uVar5 = uVar5 - 1;
          iVar3 = iVar3 + -1;
        } while (iVar3 != 0);
      }
      iVar4 = iVar4 % 2;
      if (iVar4 == 0) {
        iVar3 = 0;
      }
      else {
        iVar3 = FUN_ffff0000907404b4(lVar1,param_2,param_3,iVar4);
        FUN_ffff000090741454(param_2,param_3,iVar4,iVar3);
      }
    }
    else {
LAB_ffff0000907418c8:
      iVar3 = -0x16;
    }
  }
  return iVar3;
}


