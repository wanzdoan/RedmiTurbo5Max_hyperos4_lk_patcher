
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* disable_format() */

bool disable_format(void)

{
  ulong uVar1;
  char *pcVar2;
  long lVar3;
  bool bVar4;
  char cVar5;
  string *in_x2;
  ushort local_58;
  undefined1 local_56;
  void *local_48;
  byte local_40 [8];
  ulong local_38;
  char *local_30;
  long local_28;

  lVar3 = tpidr_el0;
  local_28 = *(long *)(lVar3 + 0x28);
  cVar5 = get_control_bitpos();
  if (cVar5 == '\x01') {
    bVar4 = true;
  }
  else {
    local_56 = 0;
    local_58 = 0x3002;
    android::base::GetProperty((base *)"*config.disable_format",(string *)&local_58,in_x2);
    uVar1 = (ulong)(local_40[0] >> 1);
    if ((local_40[0] & 1) != 0) {
      uVar1 = local_38;
    }
    if (uVar1 == 1) {
      pcVar2 = (char *)((ulong)local_40 | 1);
      if ((local_40[0] & 1) != 0) {
        pcVar2 = local_30;
      }
      bVar4 = *pcVar2 == '1';
    }
    else {
      bVar4 = false;
    }
    if ((local_40[0] & 1) != 0) {
      operator_delete(local_30);
    }
    if ((local_58 & 1) != 0) {
      operator_delete(local_48);
    }
  }
  if (*(long *)(lVar3 + 0x28) == local_28) {
    return bVar4;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
