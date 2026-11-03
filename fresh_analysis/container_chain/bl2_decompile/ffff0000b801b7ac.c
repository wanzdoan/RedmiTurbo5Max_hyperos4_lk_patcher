
void FUN_ffff0000b801b7ac(undefined8 param_1,int param_2)

{
  undefined8 *puVar1;
  int iVar2;
  long lVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 local_68;
  long lStack_60;
  undefined8 local_58;
  
  local_68 = DAT_ffff0000b811b268;
  lStack_60 = DAT_ffff0000b811b270;
  local_58 = DAT_ffff0000b811b278;
  uVar4 = FUN_ffff0000b801bfdc(param_1,&local_68);
  lVar3 = DAT_ffff0000b811b270;
  if (7 < uVar4) {
    FUN_ffff0000b804c33c(s_get_the_LED_index_by_name_failed_ffff0000b808a945);
    return;
  }
  uVar6 = (ulong)uVar4;
  puVar1 = (undefined8 *)(DAT_ffff0000b811b270 + uVar6 * 0x20);
  if (*(int *)(puVar1 + 2) == param_2) {
    return;
  }
  FUN_ffff0000b804c33c
            (s_set__s_level__d__d___d__ffff0000b809e42b,*puVar1,param_2,0xff,
             *(int *)((long)puVar1 + 0x14));
  iVar5 = param_2;
  if (0xfe < param_2) {
    iVar5 = 0xff;
  }
  if (*(int *)(puVar1 + 2) == iVar5) {
    FUN_ffff0000b804c33c(s_set__s_level__d_no_change__retur_ffff0000b8096332,*puVar1);
  }
  else if (-1 < iVar5) {
    iVar5 = (*(int *)((long)puVar1 + 0x14) * iVar5 + 0x7f) / 0xff;
    goto LAB_ffff0000b801b8c0;
  }
  iVar5 = 0;
LAB_ffff0000b801b8c0:
  iVar2 = *(int *)(lVar3 + uVar6 * 0x20 + 8);
  if (iVar2 == 6) {
    iVar5 = FUN_ffff0000b80776a8(DAT_ffff0000b811b278);
  }
  else if (iVar2 == 5) {
    iVar5 = FUN_ffff0000b801c34c(puVar1);
  }
  else {
    if (iVar2 != 4) {
      return;
    }
    iVar5 = FUN_ffff0000b803fa90(iVar5);
  }
  if (iVar5 == 0) {
    *(int *)(DAT_ffff0000b811b270 + uVar6 * 0x20 + 0x10) = param_2;
  }
  return;
}


