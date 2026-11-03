// ffff000090767d34  size=400  FUN_ffff000090767d34

void FUN_ffff000090767d34(long param_1)

{
  bool bVar1;
  int iVar2;
  uint uVar3;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  ulong uStack_28;
  
  iVar2 = FUN_ffff00009078395c();
  if (iVar2 != 0xfc11) {
    FUN_ffff000090709c9c(s_MRDUMP_is_not_support_now__ffff0000907f711d);
    return;
  }
  param_1 = param_1 + 1;
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  uVar3 = FUN_ffff00009077d72c(param_1);
  uVar3 = uVar3 & ((int)uVar3 >> 0x1f ^ 0xffffffffU);
  if (uVar3 == 0) {
    iVar2 = FUN_ffff000090783154(s_mrdump_allocate_size_ffff0000907eb75c,&DAT_ffff0000907d2506);
    FUN_ffff000090709bec(s_mrdump_allocate_size__>_0_ffff00009080160b);
  }
  else if (uVar3 == 2) {
    iVar2 = FUN_ffff000090783154(s_mrdump_allocate_size_ffff0000907eb75c,s_fullmem_ffff0000907e385a)
    ;
    FUN_ffff000090709bec(s_mrdump_allocate_size__>_fullmem_ffff0000907d004b);
  }
  else {
    if (uVar3 != 1) {
      if (uVar3 < 0x100) {
        bVar1 = true;
      }
      else {
        iVar2 = FUN_ffff000090783154(s_mrdump_allocate_size_ffff0000907eb75c,param_1);
        bVar1 = iVar2 != 0;
      }
      iVar2 = FUN_ffff00009077f6fc
                        (&local_60,0x40,s_mrdump_allocate_size__>___s_ffff0000907f7138,param_1);
      if (iVar2 < 0) {
        FUN_ffff00009077f2ac(&local_60,s_unknown_error_ffff0000907e8404,0x3f);
        uStack_28 = uStack_28 & 0xffffffffffffff;
      }
      FUN_ffff000090709bec(&local_60);
      if (bVar1) goto LAB_ffff000090767e2c;
      goto LAB_ffff000090767ea4;
    }
    iVar2 = FUN_ffff000090783154(s_mrdump_allocate_size_ffff0000907eb75c,s_halfmem_ffff0000907d36c8)
    ;
    FUN_ffff000090709bec(s_mrdump_allocate_size__>_halfmem_ffff0000907ffbcd);
  }
  if (iVar2 != 0) {
LAB_ffff000090767e2c:
    FUN_ffff000090709c9c(s_mrdump_allocate_size_not_set__ffff0000907d36d0);
    return;
  }
LAB_ffff000090767ea4:
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


