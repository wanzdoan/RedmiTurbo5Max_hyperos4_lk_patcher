
/* mkdir_recursively(std::string const&, unsigned int, bool, selabel_handle const*) */

void mkdir_recursively(string *param_1,uint param_2,bool param_3,selabel_handle *param_4)

{
  ulong uVar1;
  char *pcVar2;
  string sVar3;
  long lVar4;
  int iVar5;
  int *piVar6;
  void *pvVar7;
  char *__dest;
  undefined8 uVar8;
  undefined4 *puVar9;
  long lVar10;
  long lVar11;
  ulong uVar12;
  bool bVar13;
  char *pcVar14;
  ulong uVar15;
  ulong local_138;
  ulong local_130;
  char *local_128;
  ulong local_120;
  ulong uStack_118;
  char *local_110;
  stat local_100;

  lVar4 = tpidr_el0;
  local_100.__unused[2] = *(long *)(lVar4 + 0x28);
  sVar3 = *param_1;
  uVar12 = (ulong)((byte)sVar3 >> 1);
  if (((byte)sVar3 & 1) != 0) {
    uVar12 = *(ulong *)(param_1 + 8);
  }
  if (uVar12 == 0) {
    puVar9 = (undefined4 *)__errno();
    uVar8 = 0xffffffff;
    *puVar9 = 2;
    goto LAB_003ff754;
  }
  local_120 = 0;
  uStack_118 = 0;
  local_110 = (char *)0x0;
  if (((byte)sVar3 & 1) == 0) {
    uStack_118 = *(ulong *)(param_1 + 8);
    local_120 = *(ulong *)param_1;
    local_110 = *(char **)(param_1 + 0x10);
    if (param_3) goto LAB_003ff694;
LAB_003ff42c:
    std::string::push_back((char)&local_120);
LAB_003ff438:
    pcVar14 = (char *)((ulong)&local_120 | 1);
    if ((local_120 & 1) != 0) {
      pcVar14 = local_110;
    }
    local_100.st_ctim.tv_sec = 0;
    local_100.st_mtim.tv_nsec = 0;
    local_100.__unused[0] = 0;
    local_100.st_ctim.tv_nsec = 0;
    local_100.st_mtim.tv_sec = 0;
    local_100.st_atim.tv_nsec = 0;
    local_100.st_mode = 0;
    local_100.st_uid = 0;
    local_100.st_nlink = 0;
    local_100.st_rdev = 0;
    local_100.st_gid = 0;
    local_100.__pad0 = 0;
    local_100.st_blksize = 0;
    local_100.st_size = 0;
    local_100.st_atim.tv_sec = 0;
    local_100.st_blocks = 0;
    local_100.st_ino = 0;
    local_100.st_dev = 0;
    iVar5 = stat(pcVar14,&local_100);
    if (iVar5 != 0) {
      piVar6 = (int *)__errno();
      if (*piVar6 == 2) {
        uVar12 = 0;
        pcVar14 = (char *)((ulong)&local_138 | 1);
        do {
          uVar1 = local_120 >> 1 & 0x7f;
          if ((local_120 & 1) != 0) {
            uVar1 = uStack_118;
          }
          if (uVar1 <= uVar12) goto LAB_003ff718;
          uVar15 = uVar12 + 1;
          if (uVar1 < uVar15 || uVar1 - uVar15 == 0) goto LAB_003ff718;
          pcVar2 = (char *)((ulong)&local_120 | 1);
          if ((local_120 & 1) != 0) {
            pcVar2 = local_110;
          }
          pvVar7 = memchr(pcVar2 + uVar15,0x2f,uVar1 - uVar15);
          uVar15 = (long)pvVar7 - (long)pcVar2;
          if (pvVar7 == (void *)0x0 || uVar15 == 0xffffffffffffffff) goto LAB_003ff718;
          local_138 = 0;
          local_130 = 0;
          if (uVar15 <= uVar1) {
            uVar1 = uVar15;
          }
          local_128 = (char *)0x0;
          if (0xffffffffffffffef < uVar1) {
                    /* WARNING: Subroutine does not return */
            std::string::__throw_length_error_abi_nn180000_();
          }
          if (uVar1 < 0x17) {
            local_138 = (ulong)(byte)((int)uVar1 << 1);
            __dest = pcVar14;
            if (uVar15 != 0) goto LAB_003ff550;
          }
          else {
            __dest = operator_new((uVar1 | 0xf) + 1);
            local_138 = (uVar1 | 0xf) + 2;
            local_130 = uVar1;
            local_128 = __dest;
LAB_003ff550:
            memmove(__dest,pcVar2,uVar1);
          }
          __dest[uVar1] = '\0';
          local_100.st_ctim.tv_sec = 0;
          local_100.st_mtim.tv_nsec = 0;
          local_100.__unused[0] = 0;
          local_100.st_ctim.tv_nsec = 0;
          local_100.st_mtim.tv_sec = 0;
          local_100.st_atim.tv_nsec = 0;
          local_100.st_mode = 0;
          local_100.st_uid = 0;
          local_100.st_nlink = 0;
          local_100.st_rdev = 0;
          local_100.st_gid = 0;
          local_100.__pad0 = 0;
          local_100.st_blksize = 0;
          local_100.st_size = 0;
          local_100.st_atim.tv_sec = 0;
          local_100.st_blocks = 0;
          pcVar2 = pcVar14;
          if ((local_138 & 1) != 0) {
            pcVar2 = local_128;
          }
          local_100.st_ino = 0;
          local_100.st_dev = 0;
          iVar5 = stat(pcVar2,&local_100);
          if (iVar5 == 0) {
            if (((uint)local_100.st_nlink & 0xf000) == 0x4000) {
LAB_003ff634:
              bVar13 = true;
              uVar12 = uVar15;
            }
            else {
              bVar13 = false;
              *piVar6 = 0x14;
            }
          }
          else {
            if (*piVar6 == 2) {
              local_100.st_dev = 0;
              if (param_4 != (selabel_handle *)0x0) {
                pcVar2 = pcVar14;
                if ((local_138 & 1) != 0) {
                  pcVar2 = local_128;
                }
                selabel_lookup(param_4,&local_100,pcVar2,param_2);
                setfscreatecon(local_100.st_dev);
              }
              pcVar2 = pcVar14;
              if ((local_138 & 1) != 0) {
                pcVar2 = local_128;
              }
              iVar5 = mkdir(pcVar2,param_2);
              if (local_100.st_dev != 0) {
                freecon(local_100.st_dev);
                setfscreatecon(0);
              }
              if (iVar5 == 0) goto LAB_003ff634;
            }
            bVar13 = false;
          }
          if ((local_138 & 1) != 0) {
            operator_delete(local_128);
          }
        } while (bVar13);
      }
      goto joined_r0x003ff700;
    }
    if (((uint)local_100.st_nlink & 0xf000) != 0x4000) {
      puVar9 = (undefined4 *)__errno();
      *puVar9 = 0x14;
      goto joined_r0x003ff700;
    }
LAB_003ff718:
    uVar8 = 0;
  }
  else {
    std::string::__init_copy_ctor_external
              ((string *)&local_120,*(char **)(param_1 + 0x10),*(ulong *)(param_1 + 8));
    if (!param_3) goto LAB_003ff42c;
LAB_003ff694:
    pcVar14 = (char *)((ulong)&local_120 | 1);
    uVar12 = local_120 >> 1 & 0x7f;
    if ((local_120 & 1) != 0) {
      pcVar14 = local_110;
      uVar12 = uStack_118;
    }
    if (uVar12 != 0) {
      lVar11 = 0;
      do {
        lVar10 = lVar11;
        if (uVar12 + lVar10 == 0) goto LAB_003ff6e8;
        lVar11 = lVar10 + -1;
      } while (pcVar14[lVar10 + (uVar12 - 1)] != '/');
      uVar1 = uVar12 + lVar11;
      if (uVar1 != 0xffffffffffffffff) {
        if (uVar1 < uVar12) {
          pcVar14 = local_110;
          uVar15 = uVar1 + 1;
          if ((local_120 & 1) == 0) {
            local_120 = CONCAT71(local_120._1_7_,(char)((int)(uVar1 + 1) << 1));
            pcVar14 = (char *)((ulong)&local_120 | 1);
            uVar15 = uStack_118;
          }
          uStack_118 = uVar15;
          pcVar14[lVar11 + uVar12 + 1] = '\0';
        }
        else {
          std::string::append((ulong)&local_120,(char)lVar10);
        }
        goto LAB_003ff438;
      }
    }
LAB_003ff6e8:
    puVar9 = (undefined4 *)__errno();
    *puVar9 = 2;
joined_r0x003ff700:
    uVar8 = 0xffffffff;
  }
  if ((local_120 & 1) != 0) {
    operator_delete(local_110);
  }
LAB_003ff754:
  if (*(long *)(lVar4 + 0x28) != local_100.__unused[2]) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar8);
  }
  return;
}
