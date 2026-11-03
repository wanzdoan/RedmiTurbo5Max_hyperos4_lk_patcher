// ffff00009073505c  size=408  FUN_ffff00009073505c

void FUN_ffff00009073505c(long param_1,long param_2,int param_3)

{
  int iVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_18;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  if (((param_1 == 0) || (param_2 == 0)) || (param_3 != 0x48)) {
    FUN_ffff00009077de54(s__Part__illegal_ptr__p_size__d_ffff0000907d45a0,param_1);
    return;
  }
  uVar2 = FUN_ffff00009071f9e8();
  FUN_ffff00009077eb88(&local_78,0,0x48);
  if ((uVar2 & 1) == 0) {
    iVar1 = FUN_ffff00009077f434(&local_78,0x47,&DAT_ffff0000907e0965,param_1,&DAT_ffff0000907d24c4)
    ;
    if (iVar1 < 0) goto LAB_ffff0000907351cc;
    iVar1 = FUN_ffff0000907351f4(&local_78);
    if (iVar1 != 0) {
      local_18 = 0;
      FUN_ffff000090735620(&local_78,&local_18);
      FUN_ffff00009077eb88(&local_78,0,0x48);
      puVar3 = &DAT_ffff0000907d8bae;
      goto LAB_ffff000090735154;
    }
LAB_ffff000090735174:
    FUN_ffff00009077eb88(&local_78,0,0x48);
    iVar1 = FUN_ffff00009077f434(&local_78,0x47,&DAT_ffff0000907dbe5a,param_1);
    if (iVar1 < 0) goto LAB_ffff0000907351cc;
  }
  else {
    puVar3 = (undefined *)FUN_ffff00009071f608();
LAB_ffff000090735154:
    iVar1 = FUN_ffff00009077f434(&local_78,0x47,&DAT_ffff0000907e0965,param_1,puVar3);
    if (iVar1 < 0) goto LAB_ffff0000907351cc;
    iVar1 = FUN_ffff0000907351f4(&local_78);
    if (iVar1 == 0) goto LAB_ffff000090735174;
  }
  FUN_ffff00009077eb88(param_2,0,0x48);
  iVar1 = FUN_ffff00009077f434(param_2,0x47,&DAT_ffff0000907dbe5a,&local_78);
  if (-1 < iVar1) {
    return;
  }
LAB_ffff0000907351cc:
  FUN_ffff00009077de54(s_Fail_to_revise_part_name___d__ffff0000907fb711,iVar1);
  return;
}


