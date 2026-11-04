
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* set_disable_systemrestore() */

bool set_disable_systemrestore(void)

{
  ulong uVar1;
  int *piVar2;
  long lVar3;
  bool bVar4;
  string *in_x2;
  string local_58;
  undefined4 local_57;
  undefined2 local_53;
  void *local_48;
  byte local_40 [8];
  ulong local_38;
  int *local_30;
  long local_28;

  lVar3 = tpidr_el0;
  local_28 = *(long *)(lVar3 + 0x28);
  local_58 = (string)0xa;
  local_57 = 0x736c6166;
  local_53 = 0x65;
  android::base::GetProperty((base *)&DAT_0057ba68,&local_58,in_x2);
  uVar1 = (ulong)(local_40[0] >> 1);
  if ((local_40[0] & 1) != 0) {
    uVar1 = local_38;
  }
  if (uVar1 == 5) {
    piVar2 = (int *)((ulong)local_40 | 1);
    if ((local_40[0] & 1) != 0) {
      piVar2 = local_30;
    }
    bVar4 = *piVar2 == 0x736c6166 && (char)piVar2[1] == 'e';
  }
  else {
    bVar4 = false;
  }
  if ((local_40[0] & 1) != 0) {
    operator_delete(local_30);
  }
  if (((byte)local_58 & 1) != 0) {
    operator_delete(local_48);
  }
  if (*(long *)(lVar3 + 0x28) == local_28) {
    return bVar4;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
