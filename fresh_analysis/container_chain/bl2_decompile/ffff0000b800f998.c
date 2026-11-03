
void FUN_ffff0000b800f998(void)

{
  ushort uVar1;
  int iVar2;
  undefined4 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  char *pcVar6;
  ushort *puVar7;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  uVar4 = FUN_ffff0000b800f6d8();
  uVar5 = FUN_ffff0000b804d3cc(&DAT_ffff0000b8087e79);
  iVar2 = FUN_ffff0000b804d4c4(uVar4,&DAT_ffff0000b8087e79,uVar5);
  if (iVar2 != 0) {
    uVar4 = FUN_ffff0000b800f6d8();
    uVar5 = FUN_ffff0000b804d3cc(&DAT_ffff0000b8096003);
    iVar2 = FUN_ffff0000b804d4c4(uVar4,&DAT_ffff0000b8096003,uVar5);
    if (iVar2 != 0) {
      return;
    }
  }
  if ((DAT_ffff0000b811ae90 & 1) != 0) {
    return;
  }
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  iVar2 = FUN_ffff0000b800f7c0(&local_40,0);
  if (iVar2 < 0) {
    pcVar6 = s__s__d___LK_AB__read_partition_in_ffff0000b808b557;
    uVar4 = 0xf5;
  }
  else {
    puVar7 = (ushort *)
             ((long)&uStack_38 + 4U |
             (ulong)((uStack_38._4_2_ & 0xf) < (uStack_38._6_2_ & 0xf)) << 1);
    uVar1 = *puVar7;
    if ((uVar1 >> 7 & 1) != 0) {
      return;
    }
    if ((uVar1 & 0x70) == 0x70) {
      return;
    }
    *puVar7 = uVar1 & 0xff8f | uVar1 + 0x10 & 0x70;
    uVar3 = FUN_ffff0000b8046ff8(0,&local_40,0x1c);
    uStack_28 = CONCAT44(uVar3,(undefined4)uStack_28);
    iVar2 = FUN_ffff0000b800f7c0(&local_40,1);
    if (-1 < iVar2) {
      DAT_ffff0000b811ae90 = 1;
      return;
    }
    pcVar6 = s__s__d___LK_AB__partition_write_f_ffff0000b808ce13;
    uVar4 = 0x109;
  }
  FUN_ffff0000b804c33c(pcVar6,s_add_retries_remaining_ffff0000b809e279,uVar4,iVar2);
  return;
}


