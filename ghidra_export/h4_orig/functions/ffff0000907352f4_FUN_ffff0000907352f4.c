// ffff0000907352f4  size=408  FUN_ffff0000907352f4

void FUN_ffff0000907352f4(long param_1,long param_2,int param_3)

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
    FUN_ffff00009077e11c(s__Part__illegal_ptr__p_size__d_ffff0000907d45dd,param_1);
    return;
  }
  uVar2 = FUN_ffff00009071fb70();
  FUN_ffff00009077ee50(&local_78,0,0x48);
  if ((uVar2 & 1) == 0) {
    iVar1 = FUN_ffff00009077f6fc(&local_78,0x47,&DAT_ffff0000907e0a1f,param_1,&DAT_ffff0000907d2501)
    ;
    if (iVar1 < 0) goto LAB_ffff000090735464;
    iVar1 = FUN_ffff00009073548c(&local_78);
    if (iVar1 != 0) {
      local_18 = 0;
      FUN_ffff0000907358b8(&local_78,&local_18);
      FUN_ffff00009077ee50(&local_78,0,0x48);
      puVar3 = &DAT_ffff0000907d8c2f;
      goto LAB_ffff0000907353ec;
    }
LAB_ffff00009073540c:
    FUN_ffff00009077ee50(&local_78,0,0x48);
    iVar1 = FUN_ffff00009077f6fc(&local_78,0x47,&DAT_ffff0000907dbf7c,param_1);
    if (iVar1 < 0) goto LAB_ffff000090735464;
  }
  else {
    puVar3 = (undefined *)FUN_ffff00009071f790();
LAB_ffff0000907353ec:
    iVar1 = FUN_ffff00009077f6fc(&local_78,0x47,&DAT_ffff0000907e0a1f,param_1,puVar3);
    if (iVar1 < 0) goto LAB_ffff000090735464;
    iVar1 = FUN_ffff00009073548c(&local_78);
    if (iVar1 == 0) goto LAB_ffff00009073540c;
  }
  FUN_ffff00009077ee50(param_2,0,0x48);
  iVar1 = FUN_ffff00009077f6fc(param_2,0x47,&DAT_ffff0000907dbf7c,&local_78);
  if (-1 < iVar1) {
    return;
  }
LAB_ffff000090735464:
  FUN_ffff00009077e11c(s_Fail_to_revise_part_name___d__ffff0000907fb8ad,iVar1);
  return;
}


