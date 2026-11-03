
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be086434(uint *param_1,int *param_2)

{
  int iVar1;
  uint uVar2;
  byte bVar3;
  byte bVar4;
  byte bVar5;
  uint uVar6;
  char *pcVar7;
  char *pcVar8;
  uint uVar9;
  uint uVar10;
  int iVar11;
  uint *puVar12;
  ulong uVar13;
  int iVar14;
  
  param_1[0xe] = 0;
  param_1[0xf] = 0;
  param_1[0x10] = 0;
  param_1[0x11] = 0;
  param_1[10] = 0;
  param_1[0xb] = 0;
  param_1[0xc] = 0;
  param_1[0xd] = 0;
  param_1[6] = 0;
  param_1[7] = 0;
  param_1[8] = 0;
  param_1[9] = 0;
  param_1[2] = 0;
  param_1[3] = 0;
  param_1[4] = 0;
  param_1[5] = 0;
  param_1[0] = 0;
  param_1[1] = 0;
  FUN_ffff0000be060ae0(param_1,0,0x48);
  if (param_2 == (int *)0x0) {
    uVar10 = 1;
  }
  else {
    bVar3 = *(byte *)(param_2 + 2);
    bVar4 = *(byte *)((long)param_2 + 5);
    iVar1 = *param_2;
    bVar5 = *(byte *)(param_2 + 1);
    iVar14 = -1000000;
    do {
      iVar11 = iVar14;
      if ((_DAT_ffff000018010020 & 1) != 0) break;
      iVar14 = iVar11 + 1;
    } while (iVar11 != 0);
    uVar2 = *param_1;
    uVar10 = uVar2 & 0xfffff000;
    *param_1 = uVar10 | (uint)(iVar11 == 0) << 2;
    if (iVar11 == 0) {
      pcVar7 = s__s__poll_mailbox_for_hwrot_faile_ffff0000be0ab02b;
      pcVar8 = s_hwrot_mailbox_send_request_ffff0000be0b94f5;
LAB_ffff0000be08660c:
      FUN_ffff0000be05fd98(pcVar7,pcVar8);
      return;
    }
    uVar6 = (uint)bVar5 | (bVar3 & 0xf) << 8 | (uint)bVar4 << 0xc | iVar1 << 0x14;
    _DAT_ffff000018010010 = uVar6;
    if ((*(byte *)(param_2 + 2) & 0xf) != 0) {
      uVar13 = 0;
      do {
        _DAT_ffff000018010010 = param_2[uVar13 + 3];
        uVar13 = uVar13 + 1;
      } while (uVar13 < ((ulong)*(byte *)(param_2 + 2) & 0xf));
    }
    _DAT_ffff000018010030 = 1;
    iVar1 = -1000000;
    do {
      iVar14 = iVar1;
      if ((_DAT_ffff000018010080 & 1) != 0) break;
      iVar1 = iVar14 + 1;
    } while (iVar14 != 0);
    if (iVar14 == 0) {
      FUN_ffff0000be05fd98
                (s__s__poll_mailbox_failed_ffff0000be0ad0ef,
                 s_hwrot_mailbox_check_safety_ffff0000be0ae10b);
      uVar10 = 4;
    }
    else {
      uVar9 = ~_DAT_ffff000018010070;
      if (uVar6 == uVar9) {
        uVar13 = (ulong)*(byte *)(param_2 + 2) & 0xf;
        if ((int)uVar13 != 0) {
          puVar12 = (uint *)(param_2 + 3);
          do {
            if (*puVar12 != ~_DAT_ffff000018010070) {
              pcVar7 = s__s__safety_failed__expected_valu_ffff0000be0b4bda;
              uVar9 = uVar6;
              goto LAB_ffff0000be086664;
            }
            uVar13 = uVar13 - 1;
            puVar12 = puVar12 + 1;
          } while (uVar13 != 0);
        }
        _DAT_ffff000018010090 = 1;
        *param_1 = uVar10;
        iVar1 = -1000000;
        do {
          iVar14 = iVar1;
          if ((_DAT_ffff000018010050 & 1) != 0) break;
          iVar1 = iVar14 + 1;
        } while (iVar14 != 0);
        if (iVar14 != 0) {
          uVar6 = _DAT_ffff000018010040 >> 8 & 0xf;
          uVar13 = (ulong)uVar6;
          uVar10 = _DAT_ffff000018010040 >> 0xc;
          uVar9 = _DAT_ffff000018010040 >> 8;
          *(char *)(param_1 + 1) = (char)_DAT_ffff000018010040;
          *param_1 = uVar2 & 0xfff00000 | uVar10;
          *(byte *)(param_1 + 2) = (byte)param_1[2] & 0xf0 | (byte)uVar9 & 0xf;
          if (uVar6 != 0) {
            puVar12 = param_1 + 3;
            do {
              uVar13 = uVar13 - 1;
              *puVar12 = _DAT_ffff000018010040;
              puVar12 = puVar12 + 1;
            } while (uVar13 != 0);
          }
          _DAT_ffff000018010060 = 1;
          return;
        }
        pcVar7 = s__s__poll_mailbox_for_result_fail_ffff0000be0c4a66;
        pcVar8 = s_hwrot_mailbox_get_reply_ffff0000be0b82a9;
        goto LAB_ffff0000be08660c;
      }
      pcVar7 = s__s__safety_failed__expected_head_ffff0000be0b8273;
LAB_ffff0000be086664:
      FUN_ffff0000be05fd98(pcVar7,s_hwrot_mailbox_check_safety_ffff0000be0ae10b,uVar6,uVar9);
      uVar10 = 3;
    }
  }
  *param_1 = *param_1 & 0xfffff000 | uVar10;
  while ((_DAT_ffff000018010050 & 1) != 0) {
    _DAT_ffff000018010060 = 1;
  }
  while ((_DAT_ffff000018010080 & 1) != 0) {
    _DAT_ffff000018010090 = 1;
  }
  return;
}


