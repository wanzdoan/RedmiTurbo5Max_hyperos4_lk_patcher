
/* debuggerd_init(RecoveryUI*) */

void debuggerd_init(RecoveryUI *param_1)

{
  long lVar1;
  int iVar2;
  _union_1457 local_50;
  code *pcStack_48;
  ulong uStack_40;
  ulong uStack_38;
  ulong local_28;

  lVar1 = tpidr_el0;
  local_28 = *(ulong *)(lVar1 + 0x28);
  pcStack_48 = (code *)0x0;
  local_50.sa_handler = (__sighandler_t)0x0;
  uStack_38 = 0;
  uStack_40 = 0;
  DAT_0042cfc0 = param_1;
  sigemptyset((sigset_t *)&stack0xffffffffffffffc0);
  pcStack_48 = debuggerd_signal_handler;
  local_50._0_4_ = 0x18000004;
  sigaction(6,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(7,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(8,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(4,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(0xd,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(0xb,(sigaction *)&local_50,(sigaction *)0x0);
  sigaction(0x10,(sigaction *)&local_50,(sigaction *)0x0);
  iVar2 = sigaction(5,(sigaction *)&local_50,(sigaction *)0x0);
  if (*(ulong *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(iVar2);
}
