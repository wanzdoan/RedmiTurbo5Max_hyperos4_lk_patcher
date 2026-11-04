
/* android::base::WriteStringToFile(std::string const&, std::string const&, bool) */

undefined4 android::base::WriteStringToFile(string *param_1,string *param_2,bool param_3)

{
  int iVar1;
  string sVar2;
  long lVar3;
  bool bVar4;
  int iVar5;
  uint uVar6;
  int *piVar7;
  ulong uVar8;
  undefined4 *puVar9;
  undefined8 uVar10;
  undefined4 *puVar11;
  string *psVar12;
  undefined4 uVar13;
  undefined4 uVar14;
  ulong uVar15;
  int local_5c;
  long local_58;

  lVar3 = tpidr_el0;
  local_58 = *(long *)(lVar3 + 0x28);
  iVar1 = 0x80241;
  if (!param_3) {
    iVar1 = 0x88241;
  }
  do {
    psVar12 = param_2 + 1;
    if (((byte)*param_2 & 1) != 0) {
      psVar12 = *(string **)(param_2 + 0x10);
    }
    iVar5 = open((char *)psVar12,iVar1,0x1b6);
  } while ((iVar5 == -1) && (piVar7 = (int *)__errno(), *piVar7 == 4));
  local_5c = -1;
  uVar8 = unique_fd_impl<android::base::DefaultCloser>::reset
                    ((unique_fd_impl<android::base::DefaultCloser> *)&local_5c,iVar5,(void *)0x0);
  iVar1 = local_5c;
  if (local_5c == -1) {
    puVar11 = (undefined4 *)__errno();
    uVar14 = *puVar11;
    uVar13 = 0;
    puVar9 = puVar11;
  }
  else {
    sVar2 = *param_1;
    uVar15 = (ulong)((byte)sVar2 >> 1);
    if (((byte)sVar2 & 1) != 0) {
      uVar15 = *(ulong *)(param_1 + 8);
    }
    if (uVar15 == 0) {
LAB_0040acc0:
      uVar13 = 1;
    }
    else {
      psVar12 = *(string **)(param_1 + 0x10);
      if (((byte)sVar2 & 1) == 0) {
        psVar12 = param_1 + 1;
      }
      do {
        while (uVar8 = __write_chk(iVar1,psVar12,uVar15,0xffffffffffffffff),
              uVar8 != 0xffffffffffffffff) {
          bVar4 = uVar15 < uVar8;
          uVar15 = uVar15 - uVar8;
          if (bVar4) {
                    /* WARNING: Subroutine does not return */
            __ubsan_handle_sub_overflow_minimal_abort();
          }
          psVar12 = psVar12 + uVar8;
          if (uVar15 == 0) goto LAB_0040acc0;
        }
        piVar7 = (int *)__errno();
        iVar5 = *piVar7;
      } while (iVar5 == 4);
      psVar12 = param_2 + 1;
      if (((byte)*param_2 & 1) != 0) {
        psVar12 = *(string **)(param_2 + 0x10);
      }
      uVar6 = unlink((char *)psVar12);
      uVar8 = (ulong)uVar6;
      uVar13 = 0;
      *piVar7 = iVar5;
    }
    iVar1 = local_5c;
    puVar9 = (undefined4 *)__errno(uVar8);
    uVar14 = *puVar9;
    puVar11 = puVar9;
    if (iVar1 != -1) {
      if (PTR_android_fdsan_close_with_tag_00429ae8 == (undefined *)0x0) {
        uVar6 = close(iVar1);
        puVar11 = (undefined4 *)(ulong)uVar6;
      }
      else {
        uVar10 = android_fdsan_create_owner_tag(3,&local_5c);
        puVar11 = (undefined4 *)android_fdsan_close_with_tag(iVar1,uVar10);
      }
    }
  }
  *puVar9 = uVar14;
  if (*(long *)(lVar3 + 0x28) != local_58) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(puVar11);
  }
  return uVar13;
}
