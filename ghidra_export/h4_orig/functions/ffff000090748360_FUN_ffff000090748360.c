// ffff000090748360  size=216  FUN_ffff000090748360

undefined8 FUN_ffff000090748360(undefined8 param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 local_38;
  
  uVar1 = FUN_ffff0000907471b4();
  lVar3 = FUN_ffff0000907471c8(1);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s___s__incorrect_region_idx___d__ffff0000907efea8,
               s_conninfra_post_allocation_ffff0000907f2fdf,1);
    uVar4 = 0xfffffffe;
  }
  else {
    local_38 = 0;
    iVar2 = FUN_ffff000090746fd0(param_1,s__soc_consys_ffff0000907f67ca,s__consys_ffff0000907d7d3b);
    if (-1 < iVar2) {
      FUN_ffff0000907470a0
                (param_1,iVar2,s_connsys_emi_addr_ffff0000907fbbd3,s_emi_addr_ffff0000907ff439,
                 lVar3 + 0x10,&local_38);
      FUN_ffff000090747118
                (param_1,iVar2,s_connsys_emi_size_ffff0000907e3187,s_emi_size_ffff0000907efec8,uVar1
                );
    }
    uVar4 = 0;
  }
  return uVar4;
}


