// ffff000090726cf0  size=812  FUN_ffff000090726cf0

undefined4 FUN_ffff000090726cf0(long param_1)

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
  iVar2 = FUN_ffff00009076d75c(param_1,s__trusty_ffff0000907e8f0f);
  if (((iVar2 < 0) ||
      (lVar4 = FUN_ffff00009076daec(param_1,iVar2,s_status_ffff0000907edb6a,&local_60), lVar4 == 0))
     || ((int)local_60 < 1)) {
code_r0xffff000090726da8:
    local_50 = DAT_ffff000090818c68;
    local_48 = PTR_s_status_ffff000090818c70;
    local_40 = DAT_ffff000090818c78;
    local_38 = DAT_ffff000090818c80;
    local_60 = PTR_s_compatible_ffff000090818c58;
    local_58 = DAT_ffff000090818c60;
    if (DAT_ffff00009086d398 == '\x01') goto LAB_ffff000090726dd4;
  }
  else {
    iVar2 = FUN_ffff00009077ede4(lVar4 + 0xc,&DAT_ffff0000907df5a6);
    if ((iVar2 == 0) ||
       (iVar2 = FUN_ffff00009077ede4(lVar4 + 0xc,&DAT_ffff0000907d27f0), iVar2 == 0))
    goto code_r0xffff000090726da8;
    DAT_ffff00009086d398 = '\x01';
LAB_ffff000090726dd4:
    local_60 = PTR_s_compatible_ffff000090818c58;
    local_58 = DAT_ffff000090818c60;
    local_50 = DAT_ffff000090818c68;
    local_48 = PTR_s_status_ffff000090818c70;
    local_40 = DAT_ffff000090818c78;
    local_38 = DAT_ffff000090818c80;
    if (param_1 == 0) {
      FUN_ffff00009077de54(s__GZ_INIT__FDT__fdt_not_found_ffff0000907df6bb);
      bVar1 = false;
      uVar6 = 0xfffffffe;
      goto LAB_ffff000090726f64;
    }
    iVar2 = FUN_ffff00009076d75c(param_1,s__trusty_ffff0000907e8f0f);
    if (iVar2 < 0) goto LAB_ffff000090726f5c;
    lVar4 = FUN_ffff00009076daec(param_1,iVar2,s_compatible_ffff0000907d7429,&local_50);
    if ((lVar4 != 0) && (-1 < (int)local_50)) {
      local_58 = FUN_ffff00009077d24c();
      if (local_58 != 0) {
        FUN_ffff00009077e858(local_58,lVar4 + 0xc,(long)(int)local_50);
        goto LAB_ffff000090726e34;
      }
LAB_ffff000090726fb8:
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_malloc_for__s_ffff0000907dc4ac,s__trusty_ffff0000907e8f0f,
                 *ppuVar5);
      bVar1 = false;
      uVar6 = 0xfffffff7;
      goto LAB_ffff000090726f64;
    }
LAB_ffff000090726e34:
    lVar4 = FUN_ffff00009076daec(param_1,iVar2,local_48,&local_38);
    if ((lVar4 != 0) && (-1 < (int)local_38)) {
      local_40 = FUN_ffff00009077d24c();
      if (local_40 == 0) {
        ppuVar5 = &local_48;
        goto LAB_ffff000090726fb8;
      }
      FUN_ffff00009077e858(local_40,lVar4 + 0xc,(long)(int)local_38);
    }
    iVar2 = FUN_ffff00009076e8c4(param_1,iVar2);
    if (iVar2 != 0) {
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_failed_to_del_ffff0000907ddc50,s__trusty_ffff0000907e8f0f,
                 iVar2);
      bVar1 = false;
      uVar6 = 0xffffffd5;
      goto LAB_ffff000090726f64;
    }
    iVar2 = FUN_ffff00009076d75c(param_1,&DAT_ffff0000907ea66c);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_path___s__not_ffff0000907fec71,&DAT_ffff0000907ea66c);
      bVar1 = false;
      uVar6 = 0xffffffee;
      goto LAB_ffff000090726f64;
    }
    iVar2 = FUN_ffff00009076e878(param_1,iVar2,s_trusty_ffff0000907d5d0a);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_failed_to_rec_ffff0000907f0f44,s__trusty_ffff0000907e8f0f);
      bVar1 = false;
      uVar6 = 0xffffffe1;
      goto LAB_ffff000090726f64;
    }
    if ((-1 < (int)local_50) &&
       (iVar3 = FUN_ffff00009076e40c(param_1,iVar2,local_60,local_58), iVar3 != 0)) {
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_unable_to_set_ffff00009080217f,s__trusty_ffff0000907e8f0f,
                 local_60,iVar3);
    }
    if ((-1 < (int)local_38) &&
       (iVar2 = FUN_ffff00009076e40c(param_1,iVar2,local_48,local_40), iVar2 != 0)) {
      FUN_ffff00009077de54
                (s__GZ_INIT__FDT__fdt_unable_to_set_ffff00009080217f,s__trusty_ffff0000907e8f0f,
                 local_48,iVar2);
    }
  }
LAB_ffff000090726f5c:
  uVar6 = 0;
  bVar1 = true;
LAB_ffff000090726f64:
  if (local_58 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  if (local_40 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  if (!bVar1) {
    FUN_ffff00009077de54(s__GZ_INIT__FDT__gz_fdt_cleanup_tr_ffff0000907e44d2,uVar6);
  }
  return uVar6;
}


