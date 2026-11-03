// ffff000090767a9c  size=400  FUN_ffff000090767a9c

void FUN_ffff000090767a9c(long param_1)

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
  
  iVar2 = FUN_ffff000090783694();
  if (iVar2 != 0xfc11) {
    FUN_ffff000090709d10(s_MRDUMP_is_not_support_now__ffff0000907f6f93);
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
  uVar3 = FUN_ffff00009077d464(param_1);
  uVar3 = uVar3 & ((int)uVar3 >> 0x1f ^ 0xffffffffU);
  if (uVar3 == 0) {
    iVar2 = FUN_ffff000090782e8c(s_mrdump_allocate_size_ffff0000907eb5d9,&DAT_ffff0000907d24c9);
    FUN_ffff000090709c60(s_mrdump_allocate_size__>_0_ffff00009080142f);
  }
  else if (uVar3 == 2) {
    iVar2 = FUN_ffff000090782e8c(s_mrdump_allocate_size_ffff0000907eb5d9,s_fullmem_ffff0000907e3789)
    ;
    FUN_ffff000090709c60(s_mrdump_allocate_size__>_fullmem_ffff0000907d002c);
  }
  else {
    if (uVar3 != 1) {
      if (uVar3 < 0x100) {
        bVar1 = true;
      }
      else {
        iVar2 = FUN_ffff000090782e8c(s_mrdump_allocate_size_ffff0000907eb5d9,param_1);
        bVar1 = iVar2 != 0;
      }
      iVar2 = FUN_ffff00009077f434
                        (&local_60,0x40,s_mrdump_allocate_size__>___s_ffff0000907f6fae,param_1);
      if (iVar2 < 0) {
        FUN_ffff00009077efe4(&local_60,s_unknown_error_ffff0000907e82c0,0x3f);
        uStack_28 = uStack_28 & 0xffffffffffffff;
      }
      FUN_ffff000090709c60(&local_60);
      if (bVar1) goto LAB_ffff000090767b94;
      goto LAB_ffff000090767c0c;
    }
    iVar2 = FUN_ffff000090782e8c(s_mrdump_allocate_size_ffff0000907eb5d9,s_halfmem_ffff0000907d368b)
    ;
    FUN_ffff000090709c60(s_mrdump_allocate_size__>_halfmem_ffff0000907ffa25);
  }
  if (iVar2 != 0) {
LAB_ffff000090767b94:
    FUN_ffff000090709d10(s_mrdump_allocate_size_not_set__ffff0000907d3693);
    return;
  }
LAB_ffff000090767c0c:
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


