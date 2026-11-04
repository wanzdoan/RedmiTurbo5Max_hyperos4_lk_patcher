
/* MiScreenRecoveryUI::EndProgress() */

void __thiscall MiScreenRecoveryUI::EndProgress(MiScreenRecoveryUI *this)

{
  ulong uVar1;
  MiScreenRecoveryUI *pMVar2;

  std::mutex::lock();
  if (this[0xd18] != (MiScreenRecoveryUI)0x0) {
    uVar1 = (ulong)*(uint *)(this + 0xbd0);
    this[0xd18] = (MiScreenRecoveryUI)0x0;
    *(undefined8 *)(this + 0xbb0) = 0;
    *(undefined4 *)(this + 3000) = 0;
    *(undefined8 *)(this + 0xbc8) = 0;
    *(undefined8 *)(this + 0xbc0) = 0;
    if (0 < (int)*(uint *)(this + 0xbd0)) {
      pMVar2 = this + 0xbe8;
      do {
        uVar1 = uVar1 - 1;
        *(undefined8 *)(pMVar2 + -0x10) = 0;
        *(undefined4 *)(pMVar2 + -8) = 0;
        *(undefined8 *)pMVar2 = 0;
        *(undefined8 *)(pMVar2 + 8) = 0;
        pMVar2 = pMVar2 + 0x20;
      } while (uVar1 != 0);
    }
    *(undefined4 *)(this + 0xbd0) = 0;
    (**(code **)(*(long *)this + 0x248))(this);
  }
  std::mutex::unlock();
  return;
}
