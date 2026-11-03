
undefined4 FUN_ffff000090726f34(long param_1)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  undefined **ppuVar5;
  undefined4 uVar6;
  undefined *local_60;
  long local_58;
  undefined8 local_50;
  undefined *local_48;
  long local_40;
  undefined8 local_38;
  
  ppuVar5 = &local_60;
  local_60 = (undefined *)((ulong)local_60 & 0xffffffff00000000);
  iVar2 = FUN_ffff00009076da24(param_1,s__trusty_ffff0000907e9053);
  if (((iVar2 < 0) ||
      (lVar4 = FUN_ffff00009076ddb4(param_1,iVar2,s_status_ffff0000907edcdb,&local_60), lVar4 == 0))
     || ((int)local_60 < 1)) {
code_r0xffff000090726fec:
    local_50 = DAT_ffff000090818c88;
    local_48 = PTR_s_status_ffff000090818c90;
    local_40 = DAT_ffff000090818c98;
    local_38 = DAT_ffff000090818ca0;
    local_60 = PTR_s_compatible_ffff000090818c78;
    local_58 = DAT_ffff000090818c80;
    if (DAT_ffff00009086d430 == '\x01') goto LAB_ffff000090727018;
  }
  else {
    iVar2 = FUN_ffff00009077f0ac(lVar4 + 0xc,&DAT_ffff0000907df649);
    if ((iVar2 == 0) ||
       (iVar2 = FUN_ffff00009077f0ac(lVar4 + 0xc,&DAT_ffff0000907d282d), iVar2 == 0))
    goto code_r0xffff000090726fec;
    DAT_ffff00009086d430 = '\x01';
LAB_ffff000090727018:
    local_60 = PTR_s_compatible_ffff000090818c78;
    local_58 = DAT_ffff000090818c80;
    local_50 = DAT_ffff000090818c88;
    local_48 = PTR_s_status_ffff000090818c90;
    local_40 = DAT_ffff000090818c98;
    local_38 = DAT_ffff000090818ca0;
    if (param_1 == 0) {
      FUN_ffff00009077e11c(s__GZ_INIT__FDT__fdt_not_found_ffff0000907df75e);
      bVar1 = false;
      uVar6 = 0xfffffffe;
      goto LAB_ffff0000907271a8;
    }
    iVar2 = FUN_ffff00009076da24(param_1,s__trusty_ffff0000907e9053);
    if (iVar2 < 0) goto LAB_ffff0000907271a0;
    lVar4 = FUN_ffff00009076ddb4(param_1,iVar2,s_compatible_ffff0000907d74bf,&local_50);
    if ((lVar4 != 0) && (-1 < (int)local_50)) {
      local_58 = FUN_ffff00009077d514();
      if (local_58 != 0) {
        FUN_ffff00009077eb20(local_58,lVar4 + 0xc,(long)(int)local_50);
        goto LAB_ffff000090727078;
      }
LAB_ffff0000907271fc:
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_malloc_for__s_ffff0000907dc582,s__trusty_ffff0000907e9053,
                 *ppuVar5);
      bVar1 = false;
      uVar6 = 0xfffffff7;
      goto LAB_ffff0000907271a8;
    }
LAB_ffff000090727078:
    lVar4 = FUN_ffff00009076ddb4(param_1,iVar2,local_48,&local_38);
    if ((lVar4 != 0) && (-1 < (int)local_38)) {
      local_40 = FUN_ffff00009077d514();
      if (local_40 == 0) {
        ppuVar5 = &local_48;
        goto LAB_ffff0000907271fc;
      }
      FUN_ffff00009077eb20(local_40,lVar4 + 0xc,(long)(int)local_38);
    }
    iVar2 = FUN_ffff00009076eb8c(param_1,iVar2);
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_failed_to_del_ffff0000907ddcf3,s__trusty_ffff0000907e9053,
                 iVar2);
      bVar1 = false;
      uVar6 = 0xffffffd5;
      goto LAB_ffff0000907271a8;
    }
    iVar2 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907ea7ef);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_path___s__not_ffff0000907fee19,&DAT_ffff0000907ea7ef);
      bVar1 = false;
      uVar6 = 0xffffffee;
      goto LAB_ffff0000907271a8;
    }
    iVar2 = FUN_ffff00009076eb40(param_1,iVar2,s_trusty_ffff0000907d5da0);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_failed_to_rec_ffff0000907f10b5,s__trusty_ffff0000907e9053);
      bVar1 = false;
      uVar6 = 0xffffffe1;
      goto LAB_ffff0000907271a8;
    }
    if ((-1 < (int)local_50) &&
       (iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,local_60,local_58), iVar3 != 0)) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_unable_to_set_ffff000090802383,s__trusty_ffff0000907e9053,
                 local_60,iVar3);
    }
    if ((-1 < (int)local_38) &&
       (iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,local_48,local_40), iVar2 != 0)) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__FDT__fdt_unable_to_set_ffff000090802383,s__trusty_ffff0000907e9053,
                 local_48,iVar2);
    }
  }
LAB_ffff0000907271a0:
  uVar6 = 0;
  bVar1 = true;
LAB_ffff0000907271a8:
  if (local_58 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  if (local_40 != 0) {
    thunk_FUN_ffff0000907c314c();
  }
  if (!bVar1) {
    FUN_ffff00009077e11c(s__GZ_INIT__FDT__gz_fdt_cleanup_tr_ffff0000907e45d6,uVar6);
  }
  return uVar6;
}


