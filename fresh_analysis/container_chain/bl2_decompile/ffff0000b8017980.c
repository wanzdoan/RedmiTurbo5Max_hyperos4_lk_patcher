
int FUN_ffff0000b8017980(uint param_1)

{
  ulong uVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  char *pcVar5;
  undefined *puVar6;
  uint *puVar7;
  code *pcVar8;
  long local_18 [3];
  
  while (local_18[0] = 0, (int)param_1 < 0) {
    iVar3 = FUN_ffff0000b8017980(0);
    if (iVar3 != 0) {
      return iVar3;
    }
    param_1 = 1;
  }
  uVar1 = (ulong)param_1;
  lVar2 = uVar1 * 0x30;
  puVar7 = (uint *)(&DAT_ffff0000b80e8cf8 + lVar2);
  if (1 < param_1) {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__PART___s__failed_to_ge_ffff0000b8087fa0,
               s_gz_part_load_image_ffff0000b80a0f55,param_1);
    return -0x29;
  }
  if ((*(code **)(&DAT_ffff0000b80e8d18 + lVar2) != (code *)0x0) &&
     (iVar3 = (**(code **)(&DAT_ffff0000b80e8d18 + lVar2))(puVar7), iVar3 != 0)) {
    return iVar3;
  }
  if (((&PTR_DAT_ffff0000b80e8d08)[uVar1 * 6] == (undefined *)0x0) ||
     ((&PTR_DAT_ffff0000b80e8d10)[uVar1 * 6] == (undefined *)0x0)) {
LAB_ffff0000b8017b40:
    iVar3 = 0;
    pcVar8 = *(code **)(&DAT_ffff0000b80e8d20 + lVar2);
  }
  else {
    if (3 < *puVar7) {
      FUN_ffff0000b804c33c
                (s__GZ_INIT__PART___s__invalid_mblo_ffff0000b808cf5b,
                 s_gz_part_load_image_ffff0000b80a0f55,param_1);
      iVar3 = -8;
      pcVar8 = *(code **)(&DAT_ffff0000b80e8d20 + lVar2);
      goto joined_r0xffff0000b8017af0;
    }
    plVar4 = (long *)FUN_ffff0000b80176a0();
    if (plVar4 == (long *)0x0) {
      FUN_ffff0000b804c33c
                (s__GZ_INIT__PART___s__failed_to_ge_ffff0000b808fa7b,
                 s_gz_part_load_image_ffff0000b80a0f55,*puVar7);
      iVar3 = -0x29;
      pcVar8 = *(code **)(&DAT_ffff0000b80e8d20 + lVar2);
      goto joined_r0xffff0000b8017af0;
    }
    if ((*plVar4 == 0) || (plVar4[1] == 0)) {
      FUN_ffff0000b804c33c
                (s__GZ_INIT__PART___s__invalid_mblo_ffff0000b809434e,
                 s_gz_part_load_image_ffff0000b80a0f55,*puVar7);
      iVar3 = -7;
      pcVar8 = *(code **)(&DAT_ffff0000b80e8d20 + lVar2);
      goto joined_r0xffff0000b8017af0;
    }
    iVar3 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,(&PTR_DAT_ffff0000b80e8d10)[uVar1 * 6],plVar4[1],
                       local_18,0xc,*plVar4,0,0);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000b8050eb8
                        ((&PTR_DAT_ffff0000b80e8d08)[uVar1 * 6],
                         (&PTR_DAT_ffff0000b80e8d10)[uVar1 * 6],
                         local_18[0] + *(long *)(&DAT_ffff0000b80e8d00 + lVar2),
                         plVar4[1] - *(long *)(&DAT_ffff0000b80e8d00 + lVar2));
      if (-1 < iVar3) goto LAB_ffff0000b8017b40;
      puVar6 = (&PTR_DAT_ffff0000b80e8d10)[uVar1 * 6];
      pcVar5 = s__GZ_INIT__PART___s__load_partiti_ffff0000b809b672;
    }
    else {
      puVar6 = (&PTR_DAT_ffff0000b80e8d10)[uVar1 * 6];
      local_18[0] = 0;
      pcVar5 = s__GZ_INIT__PART___s____s__vmm_all_ffff0000b8098b60;
    }
    FUN_ffff0000b804c33c(pcVar5,s_gz_part_load_image_ffff0000b80a0f55,puVar6);
    pcVar8 = *(code **)(&DAT_ffff0000b80e8d20 + lVar2);
  }
joined_r0xffff0000b8017af0:
  if (pcVar8 != (code *)0x0) {
    iVar3 = (*pcVar8)(puVar7,iVar3);
  }
  if (local_18[0] != 0) {
    FUN_ffff0000b804a7a4(&DAT_ffff0000b8129bd0);
  }
  return iVar3;
}


