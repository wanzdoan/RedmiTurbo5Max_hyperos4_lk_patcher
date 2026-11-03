// ffff0000907480c8  size=216  FUN_ffff0000907480c8

undefined8 FUN_ffff0000907480c8(undefined8 param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 local_38;
  
  uVar1 = FUN_ffff000090746f1c();
  lVar3 = FUN_ffff000090746f30(1);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s___s__incorrect_region_idx___d__ffff0000907efd37,
               s_conninfra_post_allocation_ffff0000907f2e53,1);
    uVar4 = 0xfffffffe;
  }
  else {
    local_38 = 0;
    iVar2 = FUN_ffff000090746d38(param_1,s__soc_consys_ffff0000907f6640,s__consys_ffff0000907d7ca5);
    if (-1 < iVar2) {
      FUN_ffff000090746e08
                (param_1,iVar2,s_connsys_emi_addr_ffff0000907fba37,s_emi_addr_ffff0000907ff291,
                 lVar3 + 0x10,&local_38);
      FUN_ffff000090746e80
                (param_1,iVar2,s_connsys_emi_size_ffff0000907e30b6,s_emi_size_ffff0000907efd57,uVar1
                );
    }
    uVar4 = 0;
  }
  return uVar4;
}


