// File offset entry: 0x1f7b4

void FUN_ffff0000b801f7b4(long param_1,long param_2,int param_3)

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
    FUN_ffff0000b804c33c(s__Part__illegal_ptr__p_size__d_ffff0000b8088149,param_1);
    return;
  }
  uVar2 = FUN_ffff0000b800fb80();
  FUN_ffff0000b804d070(&local_78,0,0x48);
  if ((uVar2 & 1) == 0) {
    iVar1 = FUN_ffff0000b804d74c(&local_78,0x47,&DAT_ffff0000b808ece5,param_1,&DAT_ffff0000b8087257)
    ;
    if (iVar1 < 0) goto LAB_ffff0000b801f924;
    iVar1 = FUN_ffff0000b801f94c(&local_78);
    if (iVar1 != 0) {
      local_18 = 0;
      FUN_ffff0000b801fa3c(&local_78,&local_18);
      FUN_ffff0000b804d070(&local_78,0,0x48);
      puVar3 = &DAT_ffff0000b808a9c4;
      goto LAB_ffff0000b801f8ac;
    }
LAB_ffff0000b801f8cc:
    FUN_ffff0000b804d070(&local_78,0,0x48);
    iVar1 = FUN_ffff0000b804d74c(&local_78,0x47,&DAT_ffff0000b808c500,param_1);
    if (iVar1 < 0) goto LAB_ffff0000b801f924;
  }
  else {
    puVar3 = (undefined *)FUN_ffff0000b800f6d8();
LAB_ffff0000b801f8ac:
    iVar1 = FUN_ffff0000b804d74c(&local_78,0x47,&DAT_ffff0000b808ece5,param_1,puVar3);
    if (iVar1 < 0) goto LAB_ffff0000b801f924;
    iVar1 = FUN_ffff0000b801f94c(&local_78);
    if (iVar1 == 0) goto LAB_ffff0000b801f8cc;
  }
  FUN_ffff0000b804d070(param_2,0,0x48);
  iVar1 = FUN_ffff0000b804d74c(param_2,0x47,&DAT_ffff0000b808c500,&local_78);
  if (-1 < iVar1) {
    return;
  }
LAB_ffff0000b801f924:
  FUN_ffff0000b804c33c(s_Fail_to_revise_part_name___d__ffff0000b809d754,iVar1);
  return;
}


